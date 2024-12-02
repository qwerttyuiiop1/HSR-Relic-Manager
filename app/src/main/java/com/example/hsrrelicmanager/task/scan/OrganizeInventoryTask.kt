package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResetRunner
import com.example.hsrrelicmanager.core.exe.TaskInstance
import com.example.hsrrelicmanager.core.exe.default
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.rules.ActionPredictor
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.ui.db.DBManager
import kotlinx.coroutines.delay

class OrganizeInventoryTask: ResetRunner() {
    inner class OrganizeInst: Instance<String>() {
        suspend fun performAction(action: String) {
            val enhanceBtn = ui.enhanceBtn
            if (action.startsWith("ENHANCE")) {
                while (enhanceBtn.isRecognized()) {
                    enhanceBtn.click()
                    delay(3000)
                    awaitTick()
                }
                val target = action.substring("ENHANCE-".length).toInt()
                val enhanceInst = EnhanceInst(enhanceUI, target)
                join(enhanceInst)
                while (!enhanceBtn.isRecognized()) {
                    ui.exitBtn.click()
                    delay(5000)
                    awaitTick()
                }
            }

            val lockBtn = ui.relicLock
            val trashBtn = ui.relicTrash
            val isLocked = lockBtn.isSelected()
            val isTrash = trashBtn.isSelected()

            if (action == "LOCK" && !isLocked) {
                if (isTrash) {
                    trashBtn.click()
                    delay(3000)
                }
                lockBtn.click()
                delay(3000)
            } else if (action == "TRASH" && !isTrash) {
                if (isLocked) {
                    lockBtn.click()
                    delay(3000)
                }
                trashBtn.click()
                delay(3000)
            } else if (action == "DEFAULT") {
                if (isLocked) {
                    lockBtn.click()
                    delay(3000)
                } else if (isTrash) {
                    trashBtn.click()
                    delay(3000)
                }
            }
            awaitTick()
        }
        override suspend fun run(): MyResult<String> {
            awaitTick()
            val dbManager = DBManager(uiCtx.ctx).open()
            val rules = dbManager.listGroups()
            val manualStatuses = dbManager.listManualStatuses()
            val predictor = ActionPredictor(rules, manualStatuses)

            join(InventoryIterator(ui) {
                TaskInstance.default {
                    fun toActionString(action: Action): String {
                        if (action is EnhanceAction) {
                            return "ENHANCE-" + action.targetLevel.toString()
                        }
                        return (action as StatusAction).targetStatus.name
                    }
                    suspend fun updateRelicInDb(id: Long) {
                        val relic = join(ScanInst(ui))
                        dbManager.updateInventory(relic.copy(id = id))
                    }
                    awaitTick()

                    val relic = join(ScanInst(ui))

                    val relic_ids = dbManager.findRelicIds(relic)
                    val manual_status = manualStatuses.firstOrNull { it.first.id in relic_ids }
                    if (manual_status != null) {
                        val relic_id = manual_status.first.id
                        val statuses = manual_status.second
                        if (statuses.isNotEmpty()) {
                            for (status in statuses) {
                                val targetLevel: Int = ((relic.level / 3 + 1) * 3).coerceAtMost(relic.rarity * 3)
                                val action: Action? = when (status) {
                                    Relic.Status.UPGRADE -> EnhanceAction(targetLevel)
                                    Relic.Status.EQUIPPED -> null
                                    else -> StatusAction(status)
                                }

                                if (action != null) {
                                    performAction(toActionString(action))
                                    updateRelicInDb(relic_id)
                                }
                            }
                            dbManager.deleteStatus(relic_id, statuses.map { it.name })
                            manualStatuses.removeIf { it.first.id == relic_id }
                        }
                    } else {
                        val id = if (relic_ids.isNotEmpty()) relic_ids[0] else null
                        val action = predictor.predict(rules, relic)
                        if (action != null) {
                            performAction(toActionString(action))
                            if (id != null) {
                                updateRelicInDb(id)
                            } else {
                                val relic = join(ScanInst(ui))
                                dbManager.insertInventory(relic)
                            }
                        }
                    }
                    delay(3000)
                    awaitTick()

                    MyResult.Success("Organize Inventory")
                }
            })
            return MyResult.Success("Organize Inventory")
        }
    }

    private lateinit var ui: ScanInventoryUIBinding
    private lateinit var enhanceUI: EnhanceUIBinding
    override suspend fun initialize(uiCtx: UIContext): ResetRunner {
        ui = ScanInventoryUIBinding(uiCtx)
        enhanceUI = EnhanceUIBinding(uiCtx)
        return super.initialize(uiCtx)
    }

    override fun newInstance() = OrganizeInst()

    override val task: Task
        get() = Task(true, "ORGANIZE")
}