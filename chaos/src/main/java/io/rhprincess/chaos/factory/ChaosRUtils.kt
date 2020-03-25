package io.rhprincess.chaos.factory

import android.content.Context
import android.content.res.TypedArray
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import java.io.InputStream

/**
 *  R.drawable.xxx.rDrawable
 */
val Int.rDrawable: Drawable
    get() = ChaosProvider.context.resources.getDrawable(this, ChaosProvider.context.theme)

/**
 *  R.color.xxx.rColor
 */
@Suppress("DEPRECATION")
@Deprecated("", ReplaceWith("rColor2"))
val Int.rColor: Int
    get() = ChaosProvider.context.resources.getColor(this)

/**
 * R.color.xxx.rColor2
 */
val Int.rColor2: Int
    @RequiresApi(Build.VERSION_CODES.M)
    get() = ChaosProvider.context.getColor(this)

/**
 * R.string.xxx.rString
 */
val Int.rString: String
    get() = ChaosProvider.context.getString(this)

/**
 * R.layout.xxx.rLayout
 */
val Int.rLayout: View
    get() = LayoutInflater.from(ChaosProvider.context).inflate(this, null)

/**
 * R.boolean.xxx.rBoolean
 */
val Int.rBoolean: Boolean
    get() = ChaosProvider.context.resources.getBoolean(this)

/**
 * R.dimension.xxx.rDimension
 */
val Int.rDimension: Float
    get() = ChaosProvider.context.resources.getDimension(this)

/**
 * R.font.xxx.rFont
 */
val Int.rFont: Typeface
    @RequiresApi(Build.VERSION_CODES.O)
    get() = ChaosProvider.context.resources.getFont(this)

/**
 * R.float.xxx.rFloat
 */
val Int.rFloat: Float
    @RequiresApi(Build.VERSION_CODES.Q)
    get() = ChaosProvider.context.resources.getFloat(this)

val Int.rIntArr: IntArray
    get() = ChaosProvider.context.resources.getIntArray(this)

/**
 * R.integer.xxx.rInteger
 */
val Int.rInteger: Int
    get() = ChaosProvider.context.resources.getInteger(this)

/**
 *  R.xml.xxx.rXml
 */
val Int.rXml: XmlResourceParser
    get() = ChaosProvider.context.resources.getXml(this)

/**
 * R.raw.xxx.rRaw
 */
val Int.rRaw: InputStream
    get() = ChaosProvider.context.resources.openRawResource(this)

/**
 * R.mipmap.xxx.rMipMap
 */
val Int.rMipMap: Drawable
    get() = ChaosProvider.context.resources.getDrawable(this, ChaosProvider.context.theme)

// ------------------------------------------------------------------------------------------ //

/**
 * 将此attr转为TypedArray并返回
 *
 * Add TypedArray Support
 */
fun Int.rAttr(ctx: Context): TypedArray {
    return ctx.obtainStyledAttributes(intArrayOf(this))
}

/**
 * 将某一个Attr转为Drawable
 *
 * R.attr.xxx.rDrawableAttr
 */
fun Int.rDrawableAttr(ctx: Context): Drawable? {
    val attr = this.rAttr(ctx)
    val drawable = attr.getDrawable(0)
    attr.recycle()
    return drawable
}

/**
 * 将某一个Attr转为Int类型的颜色值
 *
 * R.attr.xxx.rColorAttr
 */
fun Int.rColorAttr(ctx: Context): Int {
    val attr = this.rAttr(ctx)
    val color = attr.getColor(0, 0)
    attr.recycle()
    return color
}

/**
 * 将某一个Attr转为字符串
 *
 * R.attr.xxx.rStringAttr
 */
fun Int.rStringAttr(ctx: Context): String? {
    val attr = this.rAttr(ctx)
    val str = attr.getString(0)
    attr.recycle()
    return str
}

/**
 * 将某一个Attr转为布尔值
 *
 * R.attr.xxx.rBooleanAttr
 */
fun Int.rBooleanAttr(ctx: Context): Boolean {
    val attr = this.rAttr(ctx)
    val bool = attr.getBoolean(0, false)
    attr.recycle()
    return bool
}

/**
 * 将某一个Attr转为Dimension
 *
 * R.attr.xxx.rDimensionAttr
 */
fun Int.rDimensionAttr(ctx: Context): Float {
    val attr = this.rAttr(ctx)
    val dimension = attr.getDimension(0, 0f)
    attr.recycle()
    return dimension
}

/**
 * 将某一个Attr转为Typeface
 *
 * R.attr.xxx.rFontAttr
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Int.rFontAttr(ctx: Context): Typeface? {
    val attr = this.rAttr(ctx)
    val font = attr.getFont(0)
    attr.recycle()
    return font
}

/**
 * 将某一个Attr转为Float
 *
 * R.attr.xxx.rFloatAttr
 */
fun Int.rFloatAttr(ctx: Context): Float {
    val attr = this.rAttr(ctx)
    val float = attr.getFloat(0, 0f)
    attr.recycle()
    return float
}

/**
 * 将某一个Attr转为Integer
 *
 * R.attr.xxx.rIntegerAttr
 */
fun Int.rIntegerAttr(ctx: Context): Int {
    val attr = this.rAttr(ctx)
    val integer = attr.getInt(0, 0)
    attr.recycle()
    return integer
}