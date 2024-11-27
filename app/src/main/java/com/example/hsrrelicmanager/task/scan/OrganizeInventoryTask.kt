package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResetRunner
import com.example.hsrrelicmanager.core.exe.TaskInstance
import com.example.hsrrelicmanager.core.exe.default
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
                    var relic = join(ScanInst(ui))
                    // TODO: predict action
                    val action = "ENHANCE-3"
                    performAction(action)
                    delay(3000)
                    awaitTick()
                    relic = join(ScanInst(ui))
                    dbManager.insertInventory(relic)
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