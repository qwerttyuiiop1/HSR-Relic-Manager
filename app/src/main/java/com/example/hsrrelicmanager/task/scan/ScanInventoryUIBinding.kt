package com.example.hsrrelicmanager.task.scan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.example.hsrrelicmanager.core.components.UIContext
import com.example.hsrrelicmanager.core.components.UIElement
import com.example.hsrrelicmanager.core.components.pixDiff
import com.example.hsrrelicmanager.core.components.similarTo
import com.example.hsrrelicmanager.core.components.ui.MUIButton
import com.example.hsrrelicmanager.core.components.ui.SwipeArea
import com.example.hsrrelicmanager.core.components.ui.UIBldr
import com.example.hsrrelicmanager.core.components.ui.UIButton
import com.example.hsrrelicmanager.core.exe.MyResult
import com.example.hsrrelicmanager.core.exe.TaskInstance
import com.example.hsrrelicmanager.core.exe.default
import com.example.hsrrelicmanager.core.ext.doOnMeasure
import com.example.hsrrelicmanager.core.ext.rect
import com.example.hsrrelicmanager.databinding.LayoutInventoryBinding
import kotlinx.coroutines.delay

class ScanInventoryUIBinding private constructor(
    binding: LayoutInventoryBinding,
    private val ctx: UIContext,
) {
    companion object {
        suspend operator fun invoke(
            ctx: UIContext
        ): ScanInventoryUIBinding {
            val inflater = LayoutInflater.from(ctx.ctx)
            val binding = LayoutInventoryBinding.inflate(inflater)
            return binding.doOnMeasure(ctx) {
                ScanInventoryUIBinding(it, ctx)
            }
        }
    }

    inner class RelicBtn(
        v: View
    ): MUIButton by UIBldr(v, ctx).mButton {
        override suspend fun click() {
            val r = container.rect
            val y = rect.centerY().coerceIn(r.top, r.bottom)
            ctx.clicker.click(rect.centerX(), y)
        }
        fun select() = TaskInstance.default {
            for (i in 0 until 5) {
                if (isSelected()) {
                    return@default MyResult.Success(true)
                }
                click()
                delay(500)
                awaitTick()
            }
            MyResult.Success(false)
        }
        fun isSelected(): Boolean {
            val cx = rect.centerX()
            val cy = rect.centerY()
            val tick = ctx.tick
            val r = rect
            val px = listOf(
                tick.getPixel(cx, r.top - 3),
                tick.getPixel(cx, r.bottom + 3),
                tick.getPixel(r.left - 3, cy),
                tick.getPixel(r.right + 3, cy),
            )
            val white = 0xFFFFFFFF.toInt()
            return px.count {
                it.similarTo(white, 2)
            } >= 3
        }
    }
    inner class RelicContainer(
        v: ViewGroup
    ): UIElement by UIBldr(v as View, ctx).element {
        val icons = v.children.map {
            RelicBtn(it)
        }.toList()
        val row = icons.takeWhile {
            it.rect.top == rect.top
        }
        val gapW = row[1].rect.left - row[0].rect.right
        val gapH = icons[row.size].rect.top - row[0].rect.bottom
        val itemW = row[0].rect.width()
        val itemH = row[0].rect.height()
        fun setY(y: Int) {
            row.forEach {
                it.setPos(it.rect.left, y)
            }
        }

        fun calibrate(col: Int = 0) = TaskInstance.default {
            val cx = row[col].rect.centerX()
            // scam vertival line for 5 consecutive white pixels
            val tick = ctx.tick
            val white = 0xFFFFFFFF.toInt()

            for (y in rect.top..rect.bottom step 5) {
                val px = tick.getPixel(cx, y)
                if (px.similarTo(white, 2)) {
                    // check the previous 4 pixels
                    var countWhite = 5
                    for (i in 1..4) {
                        val prev = tick.getPixel(cx, y - i)
                        if (!prev.similarTo(white, 2)) {
                            countWhite = i
                            break
                        }
                    }
                    if (countWhite == 5) {
                        setY(y - 5 - itemH)
                        return@default MyResult.Success(true)
                    }
                    val pos = y - countWhite - itemH
                    for (i in 1..(5 - countWhite)) {
                        val next = tick.getPixel(cx, y + i)
                        if (!next.similarTo(white, 2))
                            break
                        countWhite++
                    }
                    if (countWhite == 5) {
                        setY(pos)
                        return@default MyResult.Success(true)
                    }
                }
            }
            return@default MyResult.Fail("Could not calibrate relic container")
//
//            var count = 0
//            for (y in rect.top..rect.bottom) {
//                val px = tick.getPixel(cx, y)
//                if (px.similarTo(white, 2)) {
//                    count++
//                    if (count == 5) {
//                        pos = y - 6
//                        break
//                    }
//                } else {
//                    count = 0
//                }
//            }
//
//            if (pos == -1)
//                return@default MyResult.Fail("Could not calibrate relic container")
//            setY(pos - itemH)
//            MyResult.Success(true)
        }

        fun moveNextRow() {
            val y = row[0].rect.bottom + gapH
            setY(y)
        }

        fun isOverflow() = row[0].rect.bottom > rect.bottom

        fun reset() = setY(rect.top)
    }
    val container = RelicContainer(binding.gridLayout)

    val relicName = UIBldr(binding.lblRelicName, ctx, scale = 1.0f).textArea
    val relicType = UIBldr(binding.lblType, ctx).textArea
    val relicLevel = UIBldr(binding.lblLevel, ctx).textArea
    inner class RelicRarity(
        v: View
    ): UIElement by UIBldr(v, ctx).element {
        fun getRarity(): Int {
            val rarities = listOf(
                5 to 0x9a6a5f,
                4 to 0x625f83,
                3 to 0x3e507a,
                2 to 0x4e5e6f,
            )
            val cy = rect.centerY()
            val x = rect.left + 3
            val px = ctx.tick.getPixel(x, cy)
            return rarities.minBy { (_, color) ->
                px.pixDiff(color)
            }.first
        }
    }
    val relicRarity = RelicRarity(binding.relicInfoContainer)
    inner class SelectableIcon(
        v: View
    ): UIButton by UIBldr(v, ctx).button {
        fun isSelected(): Boolean {
            val cx = rect.centerX()
            val cy = rect.centerY()
            val tick = ctx.tick
            val r = rect
            val px = listOf(
                tick.getPixel(cx, r.top),
                tick.getPixel(cx, r.bottom),
                tick.getPixel(r.left, cy),
                tick.getPixel(r.right, cy),
            )
            val white = 0xF5F5F5
            return px.all {
                it.similarTo(white, 5)
            }
        }
    }
    val relicLock = SelectableIcon(binding.icLock)
    val relicTrash = SelectableIcon(binding.icTrash)

    val mainStat = UIBldr(binding.lblMainStat, ctx).textArea
    val mainStatValue = UIBldr(binding.lblMainStatValue, ctx).textArea

    inner class SubstatIcon(
        val icon: UIElement
    ) {
        constructor(v: View): this(UIBldr(v, ctx).element)

        fun isPresent(): Boolean {
            val cx = icon.rect.centerX()
            val cy = icon.rect.centerY()
            val tick = ctx.tick
            val offsets = listOf(-3, 0, 3)
            val colors = offsets.flatMap { x ->
                offsets.map { y ->
                    tick.getPixel(cx + x, cy + y)
                }
            }
            val white = 0xFFFFFFFF.toInt()
            val countWhite = colors.count {
                it.similarTo(white, 2)
            }
            return countWhite > 5
        }
    }
    val substatIcons = listOf(
        SubstatIcon(binding.icSubStat1),
        SubstatIcon(binding.icSubStat2),
        SubstatIcon(binding.icSubStat3),
        SubstatIcon(binding.icSubStat4),
    )
    val substatLabels = listOf(
        UIBldr(binding.lblSubStat1, ctx).textArea,
        UIBldr(binding.lblSubStat2, ctx).textArea,
        UIBldr(binding.lblSubStat3, ctx).textArea,
        UIBldr(binding.lblSubStat4, ctx).textArea,
    )

    val substatValues = listOf(
        UIBldr(binding.lblSubStat1Value, ctx).textArea,
        UIBldr(binding.lblSubStat2Value, ctx).textArea,
        UIBldr(binding.lblSubStat3Value, ctx).textArea,
        UIBldr(binding.lblSubStat4Value, ctx).textArea,
    )

    val numRelics = UIBldr(binding.numRelics, ctx).textArea

    val relicEquipped = UIBldr(binding.lblRelicEquipped, ctx).textArea

    val exitBtn = UIBldr(binding.btnExit, ctx).button
    val enhanceBtn = UIBldr(binding.btnEnhance, ctx).textButton
    
    val relicScrollBar = RelicScrollBar(binding.scrollBar)
    inner class RelicScrollBar(
        scrollBar: View
    ) {
        suspend fun defaultScroll() {
            val scrollDist = (container.itemH + container.gapH) * 2
            area.swipeV(scrollDist)
        }

        fun isAtTop() = pxContainsScroll(scrollTop + 3)
        fun isAtBottom() = pxContainsScroll(scrollBot - 3)
        fun scrollToTop() = TaskInstance.default {
            awaitTick()
            val scrollDist = container.rect.height() / 2
            val fastSwiper = SwipeArea(
                container.rect, ctx, speed = 3.0f, hold = 0
            )
            while (!isAtTop()) {
                fastSwiper.swipeV(-scrollDist)
                delay(500)
                awaitTick()
            }
            fastSwiper.swipeV(-scrollDist)
            delay(500)
            fastSwiper.swipeV(-scrollDist)
            delay(3000)
            MyResult.Success(true)
        }

        val area = SwipeArea(
            container.rect,
            ctx,
            speed = 3.0f,
            hold = 500
        )

        val scrollBar = UIBldr(scrollBar, ctx).element
        val cx = scrollBar.rect.centerX()
        val scrollTop = scrollBar.rect.top
        val scrollBot = scrollBar.rect.bottom

        var scrollHeight = -1f
        var pxPerScroll = -1f
        fun init(numRelics: Int) {
            val numRows = container.row.size
            val itemSize = container.itemH + container.gapH
            val totalHeight = numRows * itemSize.toFloat()
            val containerHeight = container.rect.height()
            scrollHeight = containerHeight * (1 - containerHeight / totalHeight)
            pxPerScroll = (totalHeight - containerHeight) / scrollHeight
        }

        fun pxContainsScroll(px: Int): Boolean {
            val tick = ctx.tick
            val color = tick.getPixel(cx, px)
            val diff_left = tick.getPixel(scrollBar.rect.left - 3, px).pixDiff(color)
            val diff_right = tick.getPixel(scrollBar.rect.right + 3, px).pixDiff(color)
            return diff_left > 1750 && diff_right > 1750
        }

        fun locateScrollTop(): Int {
            var step = scrollBar.rect.height()
            // locate a pixel in the scroll bar
            var pos = -1
            while (step > 0) {
                for (i in (scrollTop + step / 2)..scrollBot step step) {
                    if (pxContainsScroll(i)) {
                        pos = i
                        step = 0
                        break
                    }
                }
                step /= 2
            }
            if (pos == -1) return -1

            // walk upwards with step 5
            step = 5
            var prev = ctx.tick.getPixel(cx, pos)
            while (pos > step) {
                val curr = ctx.tick.getPixel(cx, pos - step)
                if (curr.pixDiff(prev) > 750)
                    break
                prev = curr
                pos -= step
            }

            // return the top of the scroll bar
            prev = ctx.tick.getPixel(cx, pos)
            while (pos > 0) {
                val curr = ctx.tick.getPixel(cx, pos - 1)
                if (curr.pixDiff(prev) > 750)
                    break
                prev = curr
                pos--
            }
            return pos
        }
    }
}



//            val clicker = uiCtx.clicker
//            val scrollArea = Rect(250, 250, 750, 900)

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