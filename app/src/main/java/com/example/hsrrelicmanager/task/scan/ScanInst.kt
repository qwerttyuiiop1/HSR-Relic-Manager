package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.TaskInstance
import com.example.hsrrelicmanager.core.exe.instant
import com.example.hsrrelicmanager.core.exe.multi

class ScanInst(
    val ui: ScanInventoryUIBinding
): Instance<String>() {
    override suspend fun run(): MyResult<String> {
        awaitTick()
        val textFields = listOf(
            ui.relicName,
            ui.relicType,
            ui.relicLevel,
            ui.mainStat,
            ui.mainStatValue,
        ).map {
            TaskInstance.instant {
                MyResult.Success(it.getText().text)
            }
        }
        val substatLabels = ui.substatLabels.map {
            TaskInstance.instant {
                MyResult.Success(it.getText().text)
            }
        }
        val substatValues = ui.substatValues.map {
            TaskInstance.instant {
                MyResult.Success(it.getText().text)
            }
        }
        val inst = TaskInstance.multi(
            TaskInstance.multi(textFields),
            TaskInstance.multi(substatLabels),
            TaskInstance.multi(substatValues),
        )
        /**
         * val relicName = ui.relicName.getText().text
         *             val relicType = ui.relicType.getText().text
         *             val rarity = ui.relicRarity.getRarity()
         *             val relicLevel = ui.relicLevel.getText().text
         *             val isTrash = ui.relicTrash.isSelected()
         *             val isLocked = ui.relicLock.isSelected()
         *             val mainstat = ui.mainStat.getText().text
         *             val mainstatval = ui.mainStatValue.getText().text
         *             val substatVals = mutableListOf<String>()
         *             val substats = mutableListOf<String>()
         *             for (i in 0 until 4) {
         *                 if (!ui.substatIcons[i].isPresent()) break
         *                 substats.add(ui.substatLabels[i].getText().text)
         *                 substatVals.add(ui.substatValues[i].getText().text)
         *             }
         *             val equipped = ui.equipped.isRecognized()
         *             return "Relic: $relicName\n" +
         *                     "Type: $relicType\n" +
         *                     "Rarity: $rarity\n" +
         *                     "Level: $relicLevel\n" +
         *                     "Main Stat: $mainstat $mainstatval\n" +
         *                     "Substats: ${substats.joinToString(", ")}\n" +
         *                     "Substat Values: ${substatVals.joinToString(", ")}\n" +
         *                     "Trash: $isTrash\n" +
         *                     "Locked: $isLocked\n" +
         *                     "Equipped: $equipped"
         */
        return MyResult.Success("Complete")
    }
}