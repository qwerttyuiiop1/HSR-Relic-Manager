package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResetRunner
import com.example.hsrrelicmanager.core.exe.TaskInstance
import com.example.hsrrelicmanager.core.exe.default
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicBuilder
import com.example.hsrrelicmanager.model.rules.Filter
import com.example.hsrrelicmanager.model.rules.action.Action
import com.example.hsrrelicmanager.model.rules.action.EnhanceAction
import com.example.hsrrelicmanager.model.rules.action.StatusAction
import com.example.hsrrelicmanager.model.rules.group.ActionGroup
import com.example.hsrrelicmanager.ui.db.inventory.DBManager
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
                lockBtn.click()
                delay(1000)
            } else if (action == "TRASH" && !isTrash) {
                trashBtn.click()
                delay(1000)
            } else if (action == "DEFAULT") {
                if (isLocked) {
                    lockBtn.click()
                    delay(1000)
                } else if (isTrash) {
                    trashBtn.click()
                    delay(1000)
                }
            }
            awaitTick()
        }
        override suspend fun run(): MyResult<String> {
            awaitTick()
            val dbManager = DBManager(uiCtx.ctx).open()
            join(InventoryIterator(ui) {
                TaskInstance.default {
                    fun toActionString(action: Action): String {
                        if (action is EnhanceAction) {
                            return "ENHANCE-" + action.targetLevel.toString()
                        }

                        // action is StatusAction
                        return (action as StatusAction).targetStatus.name
                    }
                    fun applyToRelic(action: Action, bldr: RelicBuilder) {
                        if (action is EnhanceAction) {
                            bldr.level = action.targetLevel
                        } else {
                            val newStatus = mutableListOf<Relic.Status>()

                            if (Relic.Status.EQUIPPED in bldr.status) {
                                newStatus.add(Relic.Status.EQUIPPED)
                            }
                            newStatus.add((action as StatusAction).targetStatus)

                            bldr.status = newStatus
                        }
                    }
                    awaitTick()

                    val relic = join(ScanInst(ui))
                    val new_relic_builder = RelicBuilder(relic, true)
                    val dbManager = DBManager(uiCtx.ctx).open()

                    // 1. APPLY ALL RULES 1 BY 1
                    // TODO: FETCH ALL GROUPS AND ITERATE
                    // val cursor = dbManager.fetchGroups()
                    // cursor.close()

                    // HARDCODED ACTION GROUP FOR TESTING
                    val rules = mutableListOf(
                        ActionGroup(
                            -1,
                            mutableMapOf(
                                Filter.Type.RARITY to Filter.RarityFilter(mutableSetOf(2, 3, 4))
                            ),
                            0,
                            null,
                            mutableListOf(),
                            StatusAction(Relic.Status.TRASH)
                        )
                    )

                    for (rule in rules) {
                        var action = rule.checkActionToPerform(relic)

                        if (action != null) {
                            if (action is EnhanceAction) {
                                action = EnhanceAction(action.targetLevel.coerceAtMost(relic.rarity * 3))
                            }

                            performAction(toActionString(action))
                            applyToRelic(action, new_relic_builder)
                        }
                    }

                    // 2. Find relic id to check if manual statuses need to be applied
                    // If there are, apply them
                    val relic_id = dbManager.findRelicId(relic)
                    if (relic_id != -1L) {
                        val statuses = dbManager.fetchStatusForRelic(relic_id)

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
                                    applyToRelic(action, new_relic_builder)
                                }
                            }

                            dbManager.deleteStatus(relic_id, statuses.map { it.name })
                        }
                    }

                    // TODO: if upgraded, read new substats and update new_relic_builder

                    // 3. If new relic already exists, update; otherwise, insert
                    if (relic_id != -1L) {
                        dbManager.updateInventory(new_relic_builder.build())
                    } else {
                        dbManager.insertInventory(new_relic_builder.build())
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