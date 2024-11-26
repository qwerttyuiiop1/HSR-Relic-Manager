package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResList
import com.example.hsrrelicmanager.core.exe.TaskInstance
import com.example.hsrrelicmanager.core.exe.flatten
import com.example.hsrrelicmanager.core.exe.instant
import com.example.hsrrelicmanager.core.exe.multi
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicBuilder
import com.example.hsrrelicmanager.model.relics.relicNameToSet

class ScanInst(
    val ui: ScanInventoryUIBinding
): Instance<Relic>() {
    @Suppress("UNCHECKED_CAST")
    override suspend fun run(): MyResult<Relic> {
        awaitTick()
        val bldr = RelicBuilder(isPrev=true)
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
        var lastSubstat = 0
        val custom = TaskInstance.instant {
            bldr.rarity = ui.relicRarity.getRarity()
            if (ui.relicTrash.isSelected())
                bldr.mstatus.add(Relic.Status.TRASH)
            else if (ui.relicLock.isSelected())
                bldr.mstatus.add(Relic.Status.LOCK)
            else
                bldr.mstatus.add(Relic.Status.DEFAULT)
            if (ui.equipped.isRecognized())
                bldr.mstatus.add(Relic.Status.EQUIPPED)
            for (i in 0 until 4) {
                if (!ui.substatIcons[i].isPresent()) break
                lastSubstat = i
            }
            MyResult.Success(Unit)
        }
        val res = join(TaskInstance.multi(
            TaskInstance.multi(textFields),
            TaskInstance.multi(substatLabels),
            TaskInstance.multi(substatValues),
            custom,
        )).flatten()
        val textRes = (res[0] as ResList<String>).flatten()
        val substatLabelsRes = (res[1] as ResList<String>).flatten()
        val substatValuesRes = (res[2] as ResList<String>).flatten()

        bldr.set = relicNameToSet[textRes[0].lowercase()]!!
        bldr.slot = textRes[1]
        val pttrn = "\\d+".toRegex()
        bldr.level = pttrn.find(textRes[2])!!.value.toInt()
        bldr.mainstat = textRes[3]
        bldr.mainstatVal = textRes[4]
        for (i in 0..lastSubstat)
            bldr.msubstats[substatLabelsRes[i]] = substatValuesRes[i]
        return MyResult.Success(bldr.build())
    }
}