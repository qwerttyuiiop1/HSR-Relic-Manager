package com.example.hsrrelicmanager.task.scan

import android.util.Log
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResetRunner
import kotlinx.coroutines.delay
import java.util.regex.Pattern

class ScanInventoryTask: ResetRunner() {
    inner class ScanInventoryInstance: Instance<String>() {
        val pattern = Pattern.compile("\\d+")
        suspend fun getNumRelics(): Int {
            val text = ui.numRelics.getText().text
            val matcher = pattern.matcher(text)
            if (!matcher.find()) return -1
            return matcher.group().toInt()
        }

        suspend fun scanRelic(): String {
            val relicName = ui.relicName.getText().text
            val relicType = ui.relicType.getText().text
            val rarity = ui.relicRarity.getRarity()
            val relicLevel = ui.relicLevel.getText().text
            val isTrash = ui.relicTrash.isSelected()
            val isLocked = ui.relicLock.isSelected()
            val mainstat = ui.mainStat.getText().text
            val mainstatval = ui.mainStatValue.getText().text
            val substatVals = mutableListOf<String>()
            val substats = mutableListOf<String>()
            for (i in 0 until 4) {
                if (!ui.substatIcons[i].isPresent()) break
                substats.add(ui.substatLabels[i].getText().text)
                substatVals.add(ui.substatValues[i].getText().text)
            }
            val equipped = ui.equipped.isRecognized()
            return "Relic: $relicName\n" +
                    "Type: $relicType\n" +
                    "Rarity: $rarity\n" +
                    "Level: $relicLevel\n" +
                    "Main Stat: $mainstat $mainstatval\n" +
                    "Substats: ${substats.joinToString(", ")}\n" +
                    "Substat Values: ${substatVals.joinToString(", ")}\n" +
                    "Trash: $isTrash\n" +
                    "Locked: $isLocked\n" +
                    "Equipped: $equipped"
        }
        override suspend fun run(): MyResult<String> {
            awaitTick()
            val container = ui.container
            join(ui.relicScrollBar.scrollToTop())
            container.reset()
            val numRelics = getNumRelics()
            if (numRelics == -1)
                return MyResult.Fail("Could not find number of relics")

//            val numCols = container.row.size
//            val numRows = container.rect.height() / (container.itemH + container.gapH).toFloat()
            var i = 0
            var col = 0
            val numCols = container.row.size
            while (i < numRelics) {
                val res = join(container.row[col].select())
                if (!res) {
                    return MyResult.Fail("Failed to select relic")
                }
                val relic = scanRelic()
                Log.e("<Relic!!>", relic)
                if (col == numCols - 1) {
                    container.moveNextRow()
                    if (container.isOverflow()) {
                        ui.relicScrollBar.defaultScroll()
                        delay(3000)
                        awaitTick()
                        container.calibrate(col)
                        container.moveNextRow()
                    }
                    col = 0
                } else {
                    col++
                }
                i++
            }

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