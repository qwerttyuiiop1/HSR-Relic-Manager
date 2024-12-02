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
                    delay(1000)
                }
                lockBtn.click()
            } else if (action == "TRASH" && !isTrash) {
                if (isLocked) {
                    lockBtn.click()
                    delay(1000)
                }
                trashBtn.click()
            } else if (action == "DEFAULT") {
                if (isLocked) {
                    lockBtn.click()
                } else if (isTrash) {
                    trashBtn.click()
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
                    val manual_status = manualStatuses.keys.find { it.id in relic_ids }?.let {
                        Pair(it, manualStatuses[it]!!)
                    }
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
                            dbManager.deleteStatus(manual_status.first)
                            manualStatuses.remove(manual_status.first)
                            delay(3000)
                            recalibrate()
                        }
                    } else {
                        val id = if (relic_ids.isNotEmpty()) relic_ids[0] else null
                        val action = predictor.predict(rules, relic)
                        if (action != null) {
                            performAction(toActionString(action))
                            if (id != null) {
                                updateRelicInDb(id)
                            } else {
                                dbManager.insertInventory(join(ScanInst(ui)))
                            }
                            delay(3000)
                            recalibrate()
                        }
                    }
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