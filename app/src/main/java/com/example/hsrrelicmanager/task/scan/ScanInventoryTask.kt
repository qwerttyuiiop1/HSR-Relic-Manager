package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResetRunner
import com.example.hsrrelicmanager.core.exe.TaskInstance
import com.example.hsrrelicmanager.core.exe.default
import com.example.hsrrelicmanager.ui.db.inventory.DBManager
import java.util.regex.Pattern

class ScanInventoryTask: ResetRunner() {
    inner class ScanInventoryInstance: Instance<String>() {
        val pattern = Pattern.compile("\\d+")
//        suspend fun scanRelic(): String {
//            val relicName = ui.relicName.getText().text
//            val relicType = ui.relicType.getText().text
//            val rarity = ui.relicRarity.getRarity()
//            val relicLevel = ui.relicLevel.getText().text
//            val isTrash = ui.relicTrash.isSelected()
//            val isLocked = ui.relicLock.isSelected()
//            val mainstat = ui.mainStat.getText().text
//            val mainstatval = ui.mainStatValue.getText().text
//            val substatVals = mutableListOf<String>()
//            val substats = mutableListOf<String>()
//            for (i in 0 until 4) {
//                if (!ui.substatIcons[i].isPresent()) break
//                substats.add(ui.substatLabels[i].getText().text)
//                substatVals.add(ui.substatValues[i].getText().text)
//            }
//            val equipped = ui.equipped.isRecognized()
//            return "Relic: $relicName\n" +
//                    "Type: $relicType\n" +
//                    "Rarity: $rarity\n" +
//                    "Level: $relicLevel\n" +
//                    "Main Stat: $mainstat $mainstatval\n" +
//                    "Substats: ${substats.joinToString(", ")}\n" +
//                    "Substat Values: ${substatVals.joinToString(", ")}\n" +
//                    "Trash: $isTrash\n" +
//                    "Locked: $isLocked\n" +
//                    "Equipped: $equipped"
//        }
    override suspend fun run(): MyResult<String> {
            awaitTick()

            val dbManager = DBManager(uiCtx.ctx).open()
            // truncate database
            dbManager.deleteAllRelics()

            join(InventoryIterator(ui) {
                TaskInstance.default {
                    awaitTick()
                    val relic = join(ScanInst(ui))
                    // save to db
                    dbManager.insertInventory(relic)
                    MyResult.Success("Scan Inventory")
                }
            })
            return MyResult.Success("Scan Inventory")
        }
    }

    private lateinit var ui: ScanInventoryUIBinding
    override suspend fun initialize(uiCtx: UIContext): ResetRunner {
        ui = ScanInventoryUIBinding(uiCtx)
        return super.initialize(uiCtx)
    }

    override fun newInstance() = ScanInventoryInstance()

    override val task: Task
        get() = Task(true, "SCAN")

}