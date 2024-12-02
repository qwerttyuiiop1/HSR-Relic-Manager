package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.TaskInstance
import kotlinx.coroutines.delay
import java.util.regex.Pattern

class InventoryIterator(
    private val ui: ScanInventoryUIBinding,
    private val pred: InventoryIterator.() -> TaskInstance<*>
) : Instance<String>() {

    private val pattern = Pattern.compile("\\d+")
    private suspend fun getNumRelics(): Int {
        val text = ui.numRelics.getText().text
        val matcher = pattern.matcher(text)
        if (!matcher.find()) return -1
        return matcher.group().toInt()
    }
    private var col = 0

    suspend fun recalibrate() {
        awaitTick()
        join(ui.container.calibrate(col))
    }

    override suspend fun run(): MyResult<String> {
        awaitTick()
        val container = ui.container
        join(ui.relicScrollBar.scrollToTop())
        container.reset()
        val numRelics = getNumRelics()
        if (numRelics == -1)
            return MyResult.Fail("Could not find number of relics")
        var i = 0
        val numCols = container.row.size
        while (i < numRelics) {
            val res = join(container.row[col].select())
            if (!res)
                return MyResult.Fail("Failed to select relic")
            join(pred())
            if (col == numCols - 1) {
                container.moveNextRow()
                while (container.isOverflow()) {
                    ui.relicScrollBar.defaultScroll()
                    delay(5000)
                    awaitTick()
                    join(container.calibrate(col))
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
