package io.rhprincess.chaos.support.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import io.rhprincess.chaos.main.internalContext
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanBePrivate")
class Border(context: Context) : FrameLayout(context) {

    private val paint = Paint()

    var borderAlpha: Float = 0.15f
        set(value) {
            field = value
            paint.alpha = (255 * value).roundToInt()
            invalidate()
        }

    var borderRadius: Float = 15f
        set(value) {
            field = value
            invalidate()
        }

    var borderThickness: Float = 8f
        set(value) {
            field = value
            paint.strokeWidth = value
            setPadding(value.toInt(), value.toInt(), value.toInt(), value.toInt())
            invalidate()
        }

    @ColorInt
    var borderColor: Int = 0xFF000000.toInt()
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }

    init {
        setPadding(
            borderThickness.toInt(),
            borderThickness.toInt(),
            borderThickness.toInt(),
            borderThickness.toInt()
        )
        setWillNotDraw(false)
        paint.isAntiAlias = true
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderThickness
        paint.alpha = (borderAlpha * 255).roundToInt()
    }

    @ColorRes
    var colorRes: Int = 0
        @RequiresApi(Build.VERSION_CODES.M)
        set(value) {
            field = value
            borderColor = context.getColor(value)
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(
            0f, 0f, width.toFloat(), height.toFloat(),
            borderRadius, borderRadius, paint
        )
    }

}

fun ViewManager.border(init: Border.() -> Unit): Border {
    val border = Border(internalContext)
    border.init()
    border.invalidate()
    if (border.layoutParams == null) {
        addView(border, ViewGroup.LayoutParams(-2, -2))
    } else {
        addView(border, border.layoutParams)
    }
    return border
}