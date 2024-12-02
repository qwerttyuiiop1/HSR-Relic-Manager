package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResList
import com.example.hsrrelicmanager.core.exe.TaskInstance
import com.example.hsrrelicmanager.core.exe.default
import com.example.hsrrelicmanager.core.exe.flatten
import com.example.hsrrelicmanager.core.exe.instant
import com.example.hsrrelicmanager.core.exe.multi
import com.example.hsrrelicmanager.model.relics.Mainstat
import com.example.hsrrelicmanager.model.relics.Slot
import com.example.hsrrelicmanager.model.relics.Substat
import com.example.hsrrelicmanager.model.relics.Relic
import com.example.hsrrelicmanager.model.relics.RelicBuilder
import com.example.hsrrelicmanager.model.relics.RelicSet

class ScanInst(
    val ui: ScanInventoryUIBinding
): Instance<Relic>() {
    @Suppress("UNCHECKED_CAST")
    override suspend fun run(): MyResult<Relic> {
        awaitTick()
        val bldr = RelicBuilder(/*isPrev=true*/)
        val textFields = listOf(
            ui.relicLevel,
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
            if (ui.relicEquipped.isRecognized())
                bldr.mstatus.add(Relic.Status.EQUIPPED)
            for (i in 0 until 4) {
                if (!ui.substatIcons[i].isPresent()) break
                lastSubstat = i
            }
            MyResult.Success(Unit)
        }
        val relicSet = TaskInstance.default {
            var set: RelicSet?
            do {
                awaitTick()
                val name = ui.relicName.getText().text
                set = RelicSet.fromTypeName(name)
            } while (set == null)
            MyResult.Success(set)
        }
        val relicSlot = TaskInstance.default {
            var slot: Slot?
            do {
                awaitTick()
                slot = Slot.fromName(ui.relicType.getText().text)
            } while (slot == null)
            MyResult.Success(slot)
        }
        val relicMainstat = TaskInstance.default {
            var mainstat: Mainstat?
            do {
                awaitTick()
                mainstat = Mainstat.fromName(ui.mainStat.getText().text)
            } while (mainstat == null)
            MyResult.Success(mainstat)
        }
        val res = join(TaskInstance.multi(
            TaskInstance.multi(textFields),
            TaskInstance.multi(substatLabels),
            TaskInstance.multi(substatValues),
            custom,
            relicSet,
            relicSlot,
            relicMainstat,
        )).flatten()
        val textRes = (res[0] as ResList<String>).flatten()
        val substatLabelsRes = (res[1] as ResList<String>).flatten()
        val substatValuesRes = (res[2] as ResList<String>).flatten()

        bldr.set = res[4] as RelicSet
        bldr.slot = res[5] as Slot
        val pttrn = "\\d+".toRegex()
        bldr.level = pttrn.find(textRes[0])!!.value.toInt()
        bldr.mainstat = res[6] as Mainstat
        bldr.mainstatVal = textRes[1]
        for (i in 0..lastSubstat) {
            var name = substatLabelsRes[i]
            var substat = Substat.fromName(name)
            while (substat == null) {
                awaitTick()
                name = ui.substatLabels[i].getText().text
                substat = Substat.fromName(name)
            }
            substat = substat.copy(value = substatValuesRes[i])
            bldr.msubstats.add(substat)
        }
        return MyResult.Success(bldr.build())
    }
}