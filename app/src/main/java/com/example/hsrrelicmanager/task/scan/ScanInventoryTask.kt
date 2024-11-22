package com.example.hsrrelicmanager.task.scan

import android.graphics.Rect
import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResetRunner

class ScanInventoryTask: ResetRunner() {
    inner class ScanInventoryInstance: Instance<String>() {
        override suspend fun run(): MyResult<String> {
            val clicker = uiCtx.clicker
            val scrollArea = Rect(250, 250, 750, 900)

//            val scrollDists = listOf(10, 20, 30, 40, 50)
//            awaitTick()
//            for (dist in scrollDists) {
//                clicker.swipeV(scrollArea, dist)
//                delay(3 * 1000)
//                awaitTick()
//                ImageDump(uiCtx.ctx).dump(tick)
//            }

//            val scrollDurations = listOf(1.0f, 1.5f, 2.0f, 2.5f, 3.0f)
//            awaitTick()
//            ImageDump(uiCtx.ctx).dump(tick)
//            for (duration in scrollDurations) {
//                clicker.swipeV(scrollArea, 300, duration, hold = 0)
//                delay(5 * 1000)
//                awaitTick()
//                ImageDump(uiCtx.ctx).dump(tick)
//            }



            // hold: 400, speed: 3.0f
//            val holds = listOf(200, 300, 400, 500, 600)
//            awaitTick()
//            for (hold in holds) {
//                clicker.swipeV(scrollArea, 300, 3.0f, hold.toLong())
//                delay(3 * 1000)
//                awaitTick()
//                ImageDump(uiCtx.ctx).dump(tick)
//            }

            // pixels glided roughly depends on speed and distance
//            val glideTest = listOf(50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600, 650)
//            awaitTick()
//            for (dist in glideTest) {
//                clicker.swipeV(scrollArea, dist, 3.0f, hold = 100)
//                delay(5 * 1000)
//                awaitTick()
//                ImageDump(uiCtx.ctx).dump(tick)
//            }

            return MyResult.Success("Scan Inventory")
        }
    }

    override fun newInstance() = ScanInventoryInstance()

    override val task: Task
        get() = Task(true, "SCAN")

}