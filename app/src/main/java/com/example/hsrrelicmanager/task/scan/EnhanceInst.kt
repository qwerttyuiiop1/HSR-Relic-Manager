package com.example.hsrrelicmanager.task.scan

import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.ext.flattenString
import com.example.hsrrelicmanager.core.ext.norm
import kotlinx.coroutines.delay
import java.util.regex.Pattern

class EnhanceInst(
    val ui: EnhanceUIBinding,
    val target: Int
): Instance<String>() {
    val pttrn = Pattern.compile("\\d+")
    suspend fun getLevel(): Int {
        val text = ui.lblLevel.getText().text
        val matcher = pttrn.matcher(text)
        if (!matcher.find()) return -1
        return matcher.group().toInt()
    }
    override suspend fun run(): MyResult<String> {
        while (true) {
            awaitTick()
            val level = getLevel()
            if (level == -1) {
                ui.clickAnywhere.click()
                delay(1000)
                continue
            }
            if (level >= target) break
            val words = ui.lblAutoAdd.getText()
                .flattenString(sep="-").split("-")
                .mapTo(HashSet()) { it.norm }
            if (ui.lblAutoAdd.isRecognized(words)) {
                ui.lblAutoAdd.click()
                delay(1000)
                continue
            }

            if (ui.lblEnhanceBtn.isRecognized()) {
                ui.lblEnhanceBtn.click()
                delay(5000)
                continue
            }
            ui.clickAnywhere.click()
            delay(1000)
            awaitTick()
        }
        return MyResult.Success("Enhance")
    }
}