package com.example.hsrrelicmanager.task.autobattle

import android.view.LayoutInflater
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.ui.UIBldr
import com.example.hsrrelicmanager.core.ext.doOnMeasure
import com.example.hsrrelicmanager.databinding.LayoutAutobattleBinding

class AutobattleUIBinding private constructor(
    binding: LayoutAutobattleBinding,
    ctx: UIContext,
){
    companion object {
        suspend operator fun invoke(
            ctx: UIContext
        ): AutobattleUIBinding {
            val inflater = LayoutInflater.from(ctx.ctx)
            val binding = LayoutAutobattleBinding.inflate(inflater)
            return binding.doOnMeasure(ctx) {
                AutobattleUIBinding(it, ctx)
            }
        }
    }



    val lblTbPowerCost = UIBldr(binding.lblStartCost, ctx).textArea
    val btnRestart = UIBldr(binding.btnRestart, ctx).textButton
    val lblTbPower = UIBldr(binding.lblTbPower, ctx).textArea
}