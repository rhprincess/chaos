package io.rhprincess.chaos.support.clip

import android.graphics.Outline
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.ColorInt

fun View.clip(init: Outline.() -> Unit) {
    this.outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            outline?.init()
        }
    }
    this.clipToOutline = true
}

fun View.clipToCircle() {
    alpha = 0f
    post {
        clip { setOval(0, 0, this@clipToCircle.width, this@clipToCircle.height) }
        animate().alpha(1f)
    }
}

fun View.clipToRoundRect(radius: Float = 15f) {
    alpha = 0f
    post {
        clip { setRoundRect(0, 0, this@clipToRoundRect.width, this@clipToRoundRect.height, radius) }
        animate().alpha(1f)
    }
}

fun View.clipToRect(rect: Rect) {
    alpha = 0f
    post {
        clip { setRect(rect) }
        animate().alpha(1f)
    }
}

fun View.shadow(height: Float = 35f, @ColorInt color: Int = 0x75000000) {
    this.elevation = height
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this.outlineAmbientShadowColor = color
        this.outlineSpotShadowColor = color
    }
}