package com.example.hsrrelicmanager.task.autobattle

import com.example.hsrrelicmanager.core.components.Task
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.ui.TextArea
import com.example.hsrrelicmanager.core.exe.Instance
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.ResetRunner
import kotlinx.coroutines.delay
import java.util.regex.Pattern

class AutobattleTask: ResetRunner() {
    override val task = Task(true, "AUTOBATTLE")
    private lateinit var ui: AutobattleUIBinding

    class AutobattleInstance(
        val ui: AutobattleUIBinding
    ): Instance<String>() {
        private val pattern = Pattern.compile("\\d+")
        suspend fun getInt(label: TextArea): Int {
            val text = label.getText().text
            val matcher = pattern.matcher(text)
            if (!matcher.find()) return -1
            return matcher.group().toInt()
        }

        override suspend fun run(): MyResult<String> {
            while (true) {
                awaitTick()
                if (ui.btnRestart.isRecognized()) {
                    val cost = getInt(ui.lblTbPowerCost)
                    val power = getInt(ui.lblTbPower)
                    if (cost == -1 || power == -1)
                        continue

                    if (power < cost) {
                        exit(MyResult.Success("Out of power"))
                    } else {
                        ui.btnRestart.click()
                    }
                } else {
                    delay(3000)
                }
            }
        }
    }

    override suspend fun initialize(uiCtx: UIContext): ResetRunner {
        ui = AutobattleUIBinding(uiCtx)
        return super.initialize(uiCtx)
    }
    override fun newInstance() = AutobattleInstance(ui)
}