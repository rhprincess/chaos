@file:Suppress("MemberVisibilityCanBePrivate")

package io.rhprincess.chaos.main

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.*
import android.view.Window.ID_ANDROID_CONTENT
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.core.view.ViewCompat
import io.rhprincess.chaos.factory.ChaosProvider
import kotlin.math.roundToInt

inline fun <reified T : View> ViewManager.widget(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: T.() -> Unit
): T {
    val instance: T
    val wrappedContext: Context =
        if (theme != 0) ContextThemeWrapper(internalContext, theme) else internalContext
    if (styledLayout == 0) {
        val clz = T::class.java
        val mCreate = clz.getDeclaredConstructor(Context::class.java)
        mCreate.isAccessible = true
        instance = mCreate.newInstance(wrappedContext)
    } else {
        instance = LayoutInflater.from(wrappedContext).inflate(styledLayout, null) as T
    }
    instance.init()
    if (instance.layoutParams == null) {
        this.addView(instance, ViewGroup.LayoutParams(-2, -2))
    } else {
        this.addView(instance, instance.layoutParams)
    }
    return instance
}

fun ViewManager.include(managerBlock: ChaosManager) {
    this.addView(
        managerBlock.view,
        managerBlock.view.layoutParams ?: ViewGroup.LayoutParams(-2, -2)
    )
}

fun ViewManager.include(@LayoutRes layout: Int, width: Int = -2, height: Int = -2): View {
    val child = LayoutInflater.from(internalContext).inflate(layout, null)
    this.addView(child, ViewGroup.LayoutParams(width, height))
    return child
}

inline fun <reified T : ViewGroup> ViewManager.include(init: IncludeParams.() -> Unit): View {
    val wrapper = IncludeParams(this)
    wrapper.init()
    if (wrapper.view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
    LayoutParamsTool.lparams(
        T::class.java,
        wrapper.view!!,
        wrapper
    )
    return wrapper.view!!
}

// TODO: View Size Tools (Definition Started)
open class Box(
    var left: Int = 0,
    var top: Int = 0,
    var right: Int = 0,
    var bottom: Int = 0
)

open class Size(var width: Int = -2, var height: Int = -2)

@Suppress("PropertyName")
open class IncludeParams(private val manager: ViewManager) : LParams() {

    var view: View? = null
    val MATCH_PARENT: Int = -1
    val WRAP_PARENT: Int = -2
    var margin: Int = 0
        set(value) = margin(value)
    var padding: Int = 0
        set(value) = padding(value)

    @LayoutRes
    var layout: Int = 0
        set(value) {
            field = value
            view = LayoutInflater.from(manager.internalContext).inflate(value, null)
            manager.addView(view, view!!.layoutParams ?: ViewGroup.LayoutParams(-2, -2))
        }

    fun padding(init: Padding.() -> Unit) {
        if (view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
        val wrap = Padding()
        wrap.init()
        view!!.setPadding(wrap.left, wrap.top, wrap.right, wrap.bottom)
    }

    fun padding(size: Int) {
        if (view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
        view!!.setPadding(size, size, size, size)
    }

    fun margin(init: Margin.() -> Unit) {
        if (view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
        val wrap = Margin()
        wrap.init()
        view!!.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (view!!.layoutParams is ViewGroup.MarginLayoutParams) {
                    val params = view!!.layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(wrap.left, wrap.top, wrap.right, wrap.bottom)
                    view!!.requestLayout()
                }
            }
        })
    }

    fun margin(size: Int) {
        if (view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
        view!!.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                if (view!!.layoutParams is ViewGroup.MarginLayoutParams) {
                    val params = view!!.layoutParams as ViewGroup.MarginLayoutParams
                    params.setMargins(size, size, size, size)
                    view!!.requestLayout()
                }
            }
        })
    }

    inline fun <T : View> bindView(@IdRes id: Int, block: T.() -> Unit): T {
        if (view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
        return view!!.findViewById<T>(id).apply(block)
    }
}

// TODO: Simple Layout Params
open class LParams(
    var width: Int = -2,
    var height: Int = -2,
    var layoutGravity: Int = Gravity.NO_GRAVITY,
    var layoutWeight: Float = 0f
) {
    private val lrule = RelativeLayout.LayoutParams(width, height)

    var centerInParent: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.CENTER_IN_PARENT)
            else lrule.removeRule(RelativeLayout.CENTER_IN_PARENT)
        }

    var centerVertical: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.CENTER_VERTICAL)
            else lrule.removeRule(RelativeLayout.CENTER_VERTICAL)
        }

    var centerHorizontal: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.CENTER_HORIZONTAL)
            else lrule.removeRule(RelativeLayout.CENTER_HORIZONTAL)
        }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("alignParentStart"))
    var alignParentLeft: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            else lrule.removeRule(RelativeLayout.ALIGN_PARENT_LEFT)
        }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("alignParentEnd"))
    var alignParentRight: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            else lrule.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        }

    var alignParentTop: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.ALIGN_PARENT_TOP)
            else lrule.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
        }

    var alignParentBottom: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            else lrule.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        }

    var alignParentStart: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.ALIGN_PARENT_START)
            else lrule.removeRule(RelativeLayout.ALIGN_PARENT_START)
        }

    var alignParentEnd: Boolean = false
        set(value) {
            field = value
            if (value) lrule.addRule(RelativeLayout.ALIGN_PARENT_END)
            else lrule.removeRule(RelativeLayout.ALIGN_PARENT_END)
        }

    var above: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.ABOVE, value)
        }

    var below: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.BELOW, value)
        }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("startOf"))
    var leftOf: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.LEFT_OF, value)
        }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("endOf"))
    var rightOf: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.RIGHT_OF, value)
        }

    var alignBaseline: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.ALIGN_BASELINE, value)
        }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("alignStart"))
    var alignLeft: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.ALIGN_LEFT, value)
        }

    var alignTop: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.ALIGN_TOP, value)
        }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("alignEnd"))
    var alignRight: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.ALIGN_RIGHT, value)
        }

    var alignBottom: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.ALIGN_BOTTOM, value)
        }

    var startOf: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.START_OF, value)
        }

    var endOf: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.END_OF, value)
        }

    var alignStart: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.ALIGN_START, value)
        }

    var alignEnd: Int = 0
        set(value) {
            field = value
            lrule.addRule(RelativeLayout.ALIGN_END, value)
        }

    fun above(subject: Int) {
        lrule.addRule(RelativeLayout.ABOVE, subject)
    }

    fun below(subject: Int) {
        lrule.addRule(RelativeLayout.BELOW, subject)
    }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("startOf()"))
    fun leftOf(subject: Int) {
        lrule.addRule(RelativeLayout.LEFT_OF, subject)
    }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("endOf()"))
    fun rightOf(subject: Int) {
        lrule.addRule(RelativeLayout.RIGHT_OF, subject)
    }

    fun alignBaseline(subject: Int) {
        lrule.addRule(RelativeLayout.ALIGN_BASELINE, subject)
    }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("alignStart()"))
    fun alignLeft(subject: Int) {
        lrule.addRule(RelativeLayout.ALIGN_LEFT, subject)
    }

    fun alignTop(subject: Int) {
        lrule.addRule(RelativeLayout.ALIGN_TOP, subject)
    }

    @Deprecated("This attribute is not support rtl layout", ReplaceWith("alignRight()"))
    fun alignRight(subject: Int) {
        lrule.addRule(RelativeLayout.ALIGN_RIGHT, subject)
    }

    fun alignBottom(subject: Int) {
        lrule.addRule(RelativeLayout.ALIGN_BOTTOM, subject)
    }

    fun startOf(subject: Int) {
        lrule.addRule(RelativeLayout.START_OF, subject)
    }

    fun endOf(subject: Int) {
        lrule.addRule(RelativeLayout.END_OF, subject)
    }

    fun alignStart(subject: Int) {
        lrule.addRule(RelativeLayout.ALIGN_START, subject)
    }

    fun alignEnd(subject: Int) {
        lrule.addRule(RelativeLayout.ALIGN_END, subject)
    }

    fun constraintWithRules(view: View) {
        view.layoutParams = lrule
    }
}

class Padding : Box()
class Margin : Box()

fun View.padding(init: Padding.() -> Unit) {
    val wrap = Padding()
    wrap.init()
    this.setPadding(wrap.left, wrap.top, wrap.right, wrap.bottom)
}

fun View.padding(size: Int) {
    this.setPadding(size, size, size, size)
}

fun View.margin(init: Margin.() -> Unit) {
    val wrap = Margin()
    wrap.init()
    this.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            this@margin.viewTreeObserver.removeOnGlobalLayoutListener(this)
            if (this@margin.layoutParams is ViewGroup.MarginLayoutParams) {
                val params = this@margin.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins(wrap.left, wrap.top, wrap.right, wrap.bottom)
                this@margin.requestLayout()
            }
        }
    })
}

fun View.margin(size: Int) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object :
        ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            this@margin.viewTreeObserver.removeOnGlobalLayoutListener(this)
            if (this@margin.layoutParams is ViewGroup.MarginLayoutParams) {
                val params = this@margin.layoutParams as ViewGroup.MarginLayoutParams
                params.setMargins(size, size, size, size)
                this@margin.requestLayout()
            }
        }
    })
}

object LayoutParamsTool {
    fun lparams(clazz: Class<*>, view: View, wrap: LParams) {
        when (clazz.simpleName) {
            "FrameLayout" -> {
                val flp = FrameLayout.LayoutParams(wrap.width, wrap.height)
                if (wrap.layoutGravity != Gravity.NO_GRAVITY) {
                    flp.gravity = wrap.layoutGravity
                }
                if (wrap.layoutWeight != 0f) throw ChaosException.UnsupportedAttribute(
                    "layoutWeight",
                    FrameLayout::class.java
                )
                view.layoutParams = flp
            }
            "RelativeLayout" -> {
                view.layoutParams =
                    RelativeLayout.LayoutParams(wrap.width, wrap.height)
                if (wrap.layoutGravity != Gravity.NO_GRAVITY) throw ChaosException.UnsupportedAttribute(
                    "layoutGravity",
                    RelativeLayout::class.java
                )
                if (wrap.layoutWeight != 0f) throw ChaosException.UnsupportedAttribute(
                    "layoutWeight",
                    RelativeLayout::class.java
                )
                wrap.constraintWithRules(view)
            }
            "LinearLayout" -> {
                val llp = LinearLayout.LayoutParams(wrap.width, wrap.height)
                if (wrap.layoutWeight != 0f) {
                    if (wrap.width == 0 && wrap.height != 0) {
                        llp.weight = wrap.layoutWeight
                    } else if (wrap.height == 0 && wrap.width != 0) {
                        llp.weight = wrap.layoutWeight
                    } else throw ChaosException.LinearLayoutWeightFailure("You must set this view's width/height to zero before using layoutWeight attribute.")
                }
                if (wrap.layoutGravity != Gravity.NO_GRAVITY) {
                    llp.gravity = wrap.layoutGravity
                }
                view.layoutParams = llp
            }
            else -> {
                view.layoutParams = ViewGroup.LayoutParams(wrap.width, wrap.height)
                if (view is ViewGroup) {
                    val clz = if (view.parent != null) view.parent.javaClass else this.javaClass
                    if (wrap.layoutGravity != Gravity.NO_GRAVITY) throw ChaosException.UnsupportedAttribute(
                        "layoutGravity",
                        clz
                    )
                    if (wrap.layoutWeight != 0f) throw ChaosException.UnsupportedAttribute(
                        "layoutWeight",
                        clz
                    )
                }
            }
        }
    }
}

inline fun <reified T : ViewGroup> View.lparams(init: LParams.() -> Unit) {
    val wrap = LParams()
    wrap.init()
    LayoutParamsTool.lparams(
        T::class.java,
        this,
        wrap
    )
}

fun View.lparams(init: Size.() -> Unit): ViewGroup.LayoutParams {
    val wrap = Size()
    wrap.init()
    this.layoutParams = ViewGroup.LayoutParams(wrap.width, wrap.height)
    return this.layoutParams
}

fun View.size(init: Size.() -> Unit) {
    val wrap = Size()
    wrap.init()
    when (this.parent) {
        is FrameLayout -> this.layoutParams = FrameLayout.LayoutParams(wrap.width, wrap.height)
        is RelativeLayout -> this.layoutParams =
            RelativeLayout.LayoutParams(wrap.width, wrap.height)
        is LinearLayout -> this.layoutParams =
            LinearLayout.LayoutParams(wrap.width, wrap.height)
        else -> this.layoutParams = ViewGroup.LayoutParams(wrap.width, wrap.height)
    }
}

var View.margin: Int
    get() = 0
    set(value) = this.margin(value)
var View.padding: Int
    get() = 0
    set(value) = this.padding(value)

var View.backgroundColor: Int
    get() = 0
    set(value) = this.setBackgroundColor(value)

var TextView.textColor: Int
    get() = this.currentTextColor
    set(value) = this.setTextColor(value)

// TODO: View Size Tools (Definition Ended)

// TODO: Tools for ChaosView
val Int.dp: Int
    get() = (this * ChaosProvider.context.resources.displayMetrics.density + 0.5f).roundToInt()

val Float.dp: Float
    get() = this * ChaosProvider.context.resources.displayMetrics.density + 0.5f

val Int.px2dp: Int
    get() = (this / ChaosProvider.context.resources.displayMetrics.density + 0.5f).roundToInt()

val Float.px2dp: Float
    get() = this / ChaosProvider.context.resources.displayMetrics.density + 0.5f

val Int.sp: Int
    get() = (this * ChaosProvider.context.resources.displayMetrics.scaledDensity + 0.5f).roundToInt()

val Float.sp: Float
    get() = this * ChaosProvider.context.resources.displayMetrics.scaledDensity + 0.5f

val Int.px2sp: Int
    get() = (this / ChaosProvider.context.resources.displayMetrics.scaledDensity + 0.5f).roundToInt()

val Float.px2sp: Float
    get() = this / ChaosProvider.context.resources.displayMetrics.scaledDensity + 0.5f

val String.color: Int
    get() = Color.parseColor(this)

val String.colorStateList: ColorStateList
    get() = ColorStateList.valueOf(Color.parseColor(this))

fun String.toast() {
    Toast.makeText(ChaosProvider.context, this, Toast.LENGTH_SHORT).show()
}

fun toast(msg: String, duration: Int) {
    Toast.makeText(ChaosProvider.context, msg, duration).show()
}

@Suppress("MemberVisibilityCanBePrivate")
class ChaosManager(
    private val context: Context,
    private val wrappedParent: ViewGroup
) {
    val view: View = wrappedParent

    fun apply(@StyleRes theme: Int = 0): ChaosManager {
        // if our root view is a window's container, then we don't need to use setContentView method.
        // because it will dynamically add to our window's container.
        if (view.id == ID_ANDROID_CONTENT) {
            if (theme != 0) context.setTheme(theme)
        } else {
            if (context is Activity) {
                if (theme != 0) context.setTheme(theme)
                context.setContentView(wrappedParent)
            } else throw ChaosException.ApplyingFailure("The apply function only works on activity or activity's subclasses.")
        }
        return this
    }

    // continue adding view to our ChaosManager
    inline fun <reified Parent : ViewGroup> carryOn(init: Parent.() -> Unit): ChaosManager {
        (view as Parent).init()
        return this
    }

    fun removeView(child: View) {
        (view as ViewGroup).removeView(child)
    }

    fun removeViewAt(index: Int) {
        (view as ViewGroup).removeViewAt(index)
    }

    fun clear() = (view as ViewGroup).removeAllViews()

    fun bitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
        if (!ViewCompat.isLaidOut(view)) {
            throw IllegalStateException("View needs to be laid out before calling bitmap()")
        }
        return Bitmap.createBitmap(view.width, view.height, config).applyCanvas {
            translate(-view.scrollX.toFloat(), -view.scrollY.toFloat())
            view.draw(this)
        }
    }

    private fun Bitmap.applyCanvas(init: Canvas.() -> Unit): Bitmap {
        val c = Canvas(this)
        c.init()
        return this
    }

    companion object {

        inline fun <reified T : View> link(
            ctx: Context,
            @StyleRes theme: Int = 0,
            @LayoutRes styledLayout: Int = 0,
            initial: T.() -> Unit,
            initialView: (wrappedContext: Context) -> T? = { null }
        ): Linker<T> {
            val themeContext = if (theme != 0) ContextThemeWrapper(ctx, theme) else ctx
            val instance = if (styledLayout == 0) {
                initialView(themeContext)!!
            } else {
                LayoutInflater.from(themeContext).inflate(styledLayout, null) as T
            }
            instance.initial()
            if (instance.layoutParams == null) {
                instance.layoutParams = ViewGroup.LayoutParams(-2, -2)
            }
            return Linker(instance)
        }

        // Linking Factory
        class Linker<T : View>(private val view: T) {
            fun into(manager: ViewManager): T {
                if (view.layoutParams != null) {
                    manager.addView(view, view.layoutParams)
                } else {
                    manager.addView(view, ViewGroup.LayoutParams(-2, -2))
                }
                return view
            }
        }
    }
}

// Extended Variable For View
@Suppress("unused")
val View.MATCH_PARENT: Int
    get() = -1

@Suppress("unused")
val View.WRAP_PARENT: Int
    get() = -2

class ChaosUIFactory(val context: Context) {
    inline fun <reified Root : ViewGroup> withRoot(init: Root.() -> Unit): ChaosManager {
        val clazz = Root::class.java.getDeclaredConstructor(Context::class.java)
        clazz.isAccessible = true
        val father = clazz.newInstance(this)
        father.init()
        father.layoutParams = ViewGroup.LayoutParams(-1, -1)
        return ChaosManager(context, father)
    }
}

val Context.UI: ChaosUIFactory
    get() = ChaosUIFactory(this)

@Suppress("FunctionName")
fun Context.UI(autoAttach: Boolean = true, init: FrameLayout.() -> Unit): ChaosManager {
    val father = if (this is Activity && autoAttach) {
        // if this context is an activity, then we return our window's container.
        this.window.decorView.findViewById(ID_ANDROID_CONTENT)
    } else {
        // if this context is not an activity, we return a FrameLayout
        val fl = FrameLayout(this)
        if (fl.layoutParams == null) fl.layoutParams = ViewGroup.LayoutParams(-1, -1)
        fl
    }
    father.init()
    return ChaosManager(this, father)
}

@Suppress("FunctionName")
fun Activity.UI(init: FrameLayout.() -> Unit): ChaosManager {
    val father = this.window.decorView.findViewById<FrameLayout>(ID_ANDROID_CONTENT)
    father.init()
    return ChaosManager(this, father)
}

val ViewManager.internalContext: Context
    get() {
        return when (this) {
            is ViewGroup -> this.context
            else -> throw ChaosException.UnsupportedViewManager("This manager@${this::class.java.simpleName} does not have a context variable")
        }
    }