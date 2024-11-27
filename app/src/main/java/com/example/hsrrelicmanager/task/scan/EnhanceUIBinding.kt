package com.example.hsrrelicmanager.task.scan

import android.view.LayoutInflater
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.ui.UIBldr
import com.example.hsrrelicmanager.core.ext.doOnMeasure
import com.example.hsrrelicmanager.databinding.LayoutEnhanceBinding

class EnhanceUIBinding(
    binding: LayoutEnhanceBinding,
    val ctx: UIContext,
) {
    companion object {
        suspend operator fun invoke(
            ctx: UIContext
        ): EnhanceUIBinding {
            val inflater = LayoutInflater.from(ctx.ctx)
            val binding = LayoutEnhanceBinding.inflate(inflater)
            return binding.doOnMeasure(ctx) {
                EnhanceUIBinding(it, ctx)
            }
        }
    }

    val lblLevel = UIBldr(binding.lblLevel, ctx).textArea
    val lblAutoAdd = UIBldr(binding.lblAutoAdd, ctx).textButton
    val lblEnhanceBtn = UIBldr(binding.lblEnhanceBtn, ctx).textButton
    val clickAnywhere = UIBldr(binding.clickAnywhere, ctx).button
    val btnExit = UIBldr(binding.btnExit, ctx).button
}