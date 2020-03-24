package io.rhprincess.chaos.factory

import android.content.res.TypedArray
import android.content.res.XmlResourceParser
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import java.io.InputStream

/* R.drawable.xxx.rDrawable */
val Int.rDrawable: Drawable
    get() = ChaosProvider.context.resources.getDrawable(this, ChaosProvider.context.theme)


@Suppress("DEPRECATION")
@Deprecated("", ReplaceWith("rColor2"))
/* R.color.xxx.rColor */
val Int.rColor: Int
    get() = ChaosProvider.context.resources.getColor(this)

/* R.color.xxx.rColor2 */
val Int.rColor2: Int
    @RequiresApi(Build.VERSION_CODES.M)
    get() = ChaosProvider.context.getColor(this)

/* R.string.xxx.rString */
val Int.rString: String
    get() = ChaosProvider.context.getString(this)

/* R.layout.xxx.rLayout */
val Int.rLayout: View
    get() = LayoutInflater.from(ChaosProvider.context).inflate(this, null)

/* R.boolean.xxx.rBoolean */
val Int.rBoolean: Boolean
    get() = ChaosProvider.context.resources.getBoolean(this)

/* R.dimension.xxx.rDimension */
val Int.rDimension: Float
    get() = ChaosProvider.context.resources.getDimension(this)

/* R.font.xxx.rFont */
val Int.rFont: Typeface
    @RequiresApi(Build.VERSION_CODES.O)
    get() = ChaosProvider.context.resources.getFont(this)

/* R.float.xxx.rFloat */
val Int.rFloat: Float
    @RequiresApi(Build.VERSION_CODES.Q)
    get() = ChaosProvider.context.resources.getFloat(this)

val Int.rIntArr: IntArray
    get() = ChaosProvider.context.resources.getIntArray(this)

/* R.integer.xxx.rInteger */
val Int.rInteger: Int
    get() = ChaosProvider.context.resources.getInteger(this)

/* R.xml.xxx.rXml */
val Int.rXml: XmlResourceParser
    get() = ChaosProvider.context.resources.getXml(this)

/* R.raw.xxx.rRaw */
val Int.rRaw: InputStream
    get() = ChaosProvider.context.resources.openRawResource(this)

/* R.mipmap.xxx.rMipMap */
val Int.rMipMap: Drawable
    get() = ChaosProvider.context.resources.getDrawable(this, ChaosProvider.context.theme)

// ------------------------------------------------------------------------------------------ //

/* Add TypedArray Support */
val Int.rAttr: TypedArray
    get() {
        return ChaosProvider.context.obtainStyledAttributes(intArrayOf(this))
    }

val Int.rDrawableAttr: Drawable?
    get() {
        val attr = this.rAttr
        val drawable = attr.getDrawable(0)
        attr.recycle()
        return drawable
    }

val Int.rColorAttr: Int
    get() {
        val attr = this.rAttr
        val color = attr.getColor(0, 0)
        attr.recycle()
        return color
    }

val Int.rStringAttr: String?
    get() {
        val attr = this.rAttr
        val str = attr.getString(0)
        attr.recycle()
        return str
    }

val Int.rBooleanAttr: Boolean
    get() {
        val attr = this.rAttr
        val bool = attr.getBoolean(0, false)
        attr.recycle()
        return bool
    }

val Int.rDimensionAttr: Float
    get() {
        val attr = this.rAttr
        val dimension = attr.getDimension(0, 0f)
        attr.recycle()
        return dimension
    }

val Int.rFontAttr: Typeface?
    @RequiresApi(Build.VERSION_CODES.O)
    get() {
        val attr = this.rAttr
        val font = attr.getFont(0)
        attr.recycle()
        return font
    }

val Int.rFloatAttr: Float
    get() {
        val attr = this.rAttr
        val float = attr.getFloat(0, 0f)
        attr.recycle()
        return float
    }

val Int.rIntegerAttr: Int
    get() {
        val attr = this.rAttr
        val integer = attr.getInt(0, 0)
        attr.recycle()
        return integer
    }