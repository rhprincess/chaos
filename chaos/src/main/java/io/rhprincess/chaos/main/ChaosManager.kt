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
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.core.view.ViewCompat
import io.rhprincess.chaos.factory.ChaosProvider
import kotlin.math.roundToInt

/**
 * 利用该方法，您可以轻松地引用第三方控件而不需要去自定义
 *
 * 例：
 * widget<MaterialButton> {
 *      ...
 * }
 *
 * @param theme 主题
 * @param styledLayout 样式化的布局文件
 *
 * 样式化布局例：
 *
 * 文件: R.layout.material_outline_button
 *
 * <?xml version="1.0" encoding="utf-8"?>
 *     <com.google.android.material.button.MaterialButton xmlns:android="http://schemas.android.com/apk/res/android"
 *     style="@style/Widget.MaterialComponents.Button.OutlinedButton"
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content" />
 *
 * 使用方法:
 *
 * widget<MaterialButton>(styledLayout = R.layout.material_outline_button) {
 *      ...
 * }
 */
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

/**
 * 使用该方法，你可以随意引入一个已定义但未实例化的ChaosManager
 *
 * 定义ChaosUI:
 *
 * 注意，这里的autoAttach必须为false
 * val content = UI(autoAttach = false) {
 *      ...
 * }
 *
 * 引入使用
 * verticalLayout {
 *      include(content)
 * }
 */
fun ViewManager.include(managerBlock: ChaosManager) {
    this.addView(
        managerBlock.view,
        managerBlock.view.layoutParams ?: ViewGroup.LayoutParams(-2, -2)
    )
}

/**
 * 使用该方法引入一个布局文件
 */
fun ViewManager.include(@LayoutRes layout: Int, width: Int = -2, height: Int = -2): View {
    val child = LayoutInflater.from(internalContext).inflate(layout, null)
    this.addView(child, ViewGroup.LayoutParams(width, height))
    return child
}

/**
 * 使用该方法引入布局文件
 *
 * verticalLayout {
 *      include {
 *          layout = R.layout.xxx
 *          padding = xxx
 *          margin = xxx
 *          ...
 *      }
 * }
 */
inline fun <reified T : ViewGroup> ViewManager.include(init: IncludeParams.() -> Unit): View {
    val wrapper = IncludeParams(this)
    wrapper.init()
    if (wrapper.view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
    this.addView(wrapper.view, wrapper.view!!.layoutParams ?: ViewGroup.LayoutParams(-2, -2))
    return wrapper.view!!
}

fun <T : ViewGroup.LayoutParams> ViewManager.include(view: View, params: T) {
    this.addView(view, params)
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
open class IncludeParams(private val manager: ViewManager) {

    var view: View? = null
    val MATCH_PARENT: Int = -1
    val WRAP_PARENT: Int = -2
    var margin: Int = 0
        set(value) = margin(value)
    var padding: Int = 0
        set(value) = padding(value)

    /**
     * 引入布局文件的Resource ID
     */
    @LayoutRes
    var layout: Int = 0
        set(value) {
            field = value
            view = LayoutInflater.from(manager.internalContext).inflate(value, null)
            manager.addView(view, view!!.layoutParams ?: ViewGroup.LayoutParams(-2, -2))
        }

    /**
     * 设置padding
     *
     * padding {
     *      left = xxx
     *      top = xxx
     *      right = xxx
     *      bottom = xxx
     * }
     */
    fun padding(init: Padding.() -> Unit) {
        if (view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
        val wrap = Padding()
        wrap.init()
        view!!.setPadding(wrap.left, wrap.top, wrap.right, wrap.bottom)
    }

    /**
     * 统一设置left, top, right, bottom的padding值
     */
    fun padding(size: Int) {
        if (view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
        view!!.setPadding(size, size, size, size)
    }

    /**
     * 设置margin外边距
     *
     * margin {
     *      left = xxx
     *      top = xxx
     *      right = xxx
     *      bottom = xxx
     * }
     */
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

    /**
     * 统一设置left, top, right, bottom的margin值
     */
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

    /**
     * 绑定布局文件中的控件
     * bindView<Button>(R.id.btn) {
     *      ...
     * }
     */
    inline fun <T : View> bindView(@IdRes id: Int, block: T.() -> Unit): T {
        if (view == null) throw ChaosException.IncludeParamsFailure("The attribute \"layout\" not defined")
        return view!!.findViewById<T>(id).apply(block)
    }
}

class Padding : Box()
class Margin : Box()

/**
 * 设置padding内边距
 *
 * padding {
 *      left = xxx
 *      top = xxx
 *      right = xxx
 *      bottom = xxx
 * }
 */
fun View.padding(init: Padding.() -> Unit) {
    val wrap = Padding()
    wrap.init()
    this.setPadding(wrap.left, wrap.top, wrap.right, wrap.bottom)
}

/**
 * 统一设置left, top, right, bottom的padding值
 */
fun View.padding(size: Int) {
    this.setPadding(size, size, size, size)
}

/**
 * 设置margin外边距
 *
 * margin {
 *      left = xxx
 *      top = xxx
 *      right = xxx
 *      bottom = xxx
 * }
 */
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

/**
 * 统一设置left, top, right, bottom的margin值
 */
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

/**
 * 对View添加margin属性的支持
 */
var View.margin: Int
    get() = 0
    set(value) = this.margin(value)

/**
 * 对View添加padding属性的支持
 */
var View.padding: Int
    get() = 0
    set(value) = this.padding(value)

/**
 * 对View添加backgroundColor属性的支持
 */
var View.backgroundColor: Int
    get() = 0
    set(value) = this.setBackgroundColor(value)

/**
 * 对View添加textColor属性的支持
 */
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

/**
 * 将字符串转为颜色值
 *
 * 例：
 *
 * "#000000".color
 */
val String.color: Int
    get() = Color.parseColor(this)

/**
 * 将字符串转为ColorStateList
 *
 * 例：
 *
 * "#000000".colorStateList
 */
val String.colorStateList: ColorStateList
    get() = ColorStateList.valueOf(Color.parseColor(this))

/**
 * 将字符串用Toast方式输出
 */
fun String.toast() {
    Toast.makeText(ChaosProvider.context, this, Toast.LENGTH_SHORT).show()
}

/**
 * 简易toast
 * @param msg 消息
 * @param duration 时长
 */
fun toast(msg: String, duration: Int) {
    Toast.makeText(ChaosProvider.context, msg, duration).show()
}

@Suppress("MemberVisibilityCanBePrivate")
class ChaosManager(
    private val context: Context,
    private val wrappedParent: ViewGroup
) {
    val view: View = wrappedParent

    /**
     * 将ChaosUI应用到Activity当中
     */
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

    /**
     * 继续编写布局
     */
    inline fun <reified Parent : ViewGroup> carryOn(init: Parent.() -> Unit): ChaosManager {
        (view as Parent).init()
        return this
    }

    /**
     * 移除某一个View
     */
    fun removeView(child: View) {
        (view as ViewGroup).removeView(child)
    }

    /**
     * 移除某个位置的View
     */
    fun removeViewAt(index: Int) {
        (view as ViewGroup).removeViewAt(index)
    }

    /**
     * 清理所有View
     */
    fun clear() = (view as ViewGroup).removeAllViews()

    /**
     * 将布局转为Bitmap位图
     */
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

        inline fun <reified T : View> init(
            ctx: Context,
            @StyleRes theme: Int = 0,
            @LayoutRes styledLayout: Int = 0,
            initial: T.() -> Unit,
            initialView: (wrappedContext: Context) -> T? = { null }
        ): T {
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
            return instance
        }

        inline fun <reified T : View> link(
            ctx: Context,
            @StyleRes theme: Int = 0,
            @LayoutRes styledLayout: Int = 0,
            initial: T.() -> Unit,
            initialView: (wrappedContext: Context) -> T? = { null }
        ): Linker<T> = Linker(init(ctx, theme, styledLayout, initial, initialView))

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