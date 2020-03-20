package io.rhprincess.chaos.support.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import io.rhprincess.chaos.factory.rDrawable

var ImageView.imageRes: Int
    get() = 0
    set(@DrawableRes value) {
        setImageResource(value)
    }

var ImageView.imageResource: Int
    get() = 0
    set(@DrawableRes value) {
        setImageResource(value)
    }

var ImageView.imageDrawable: Drawable?
    get() = null
    set(value) {
        setImageDrawable(value)
    }

var ImageView.imageBitmap: Bitmap?
    get() = null
    set(value) {
        setImageBitmap(value)
    }

fun ImageView.level(@DrawableRes vararg drawableRes: Int) {
    val levelListDrawable = LevelListDrawable()
    var level = 0
    drawableRes.forEach {
        levelListDrawable.addLevel(level, level, it.rDrawable)
        level++
    }
    setImageDrawable(levelListDrawable)
}

fun ImageView.level(init: ImageLevel.() -> Unit) {
    val imageLevel = ImageLevel()
    imageLevel.init()
    setImageDrawable(imageLevel.getDrawable())
}

@Suppress("MemberVisibilityCanBePrivate")
class ImageLevel {
    private val levelListDrawable = LevelListDrawable()
    private var maxLevelValue = 0

    class LevelDrawable(
        var minLevel: Int = 0,
        var maxLevel: Int = 0,
        var drawable: Drawable? = null,
        @DrawableRes var drawableRes: Int = 0
    )

    fun newLevel(
        minLevel: Int = 0,
        maxLevel: Int = 0,
        drawable: Drawable? = null,
        @DrawableRes drawableRes: Int = 0
    ) {
        if (drawableRes == 0) {
            levelListDrawable.addLevel(minLevel, maxLevel, drawable)
        } else {
            levelListDrawable.addLevel(minLevel, maxLevel, drawableRes.rDrawable)
        }
        maxLevelValue = maxLevel
    }

    fun newLevel(init: LevelDrawable.() -> Unit) {
        val wrap = LevelDrawable()
        wrap.init()
        newLevel(wrap.minLevel, wrap.maxLevel, wrap.drawable, wrap.drawableRes)
        maxLevelValue = wrap.maxLevel
    }

    fun getDrawable(): LevelListDrawable = levelListDrawable
}