@file:Suppress("unused")

package io.rhprincess.chaos.main.widgets

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.view.ViewManager
import android.view.Window
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import io.rhprincess.chaos.lazy.LazyViewFactory
import io.rhprincess.chaos.main.ChaosManager
import io.rhprincess.chaos.main.internalContext

/**
 *
 *               TODO: For AnyViewManager
 *
 */

// TODO: LinearLayout
fun ViewManager.linearLayout(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: LinearLayout.() -> Unit
): LinearLayout {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { LinearLayout(it) }
        .into(this)
}

// TODO: LinearLayout
fun ViewManager.verticalLayout(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: LinearLayout.() -> Unit
): LinearLayout {
    return ChaosManager.link(internalContext, theme, styledLayout, init) {
        val ll = LinearLayout(it)
        if (ll.orientation == LinearLayout.HORIZONTAL) ll.orientation = LinearLayout.VERTICAL
        return@link ll
    }.into(this)
}

// TODO: LinearLayout
fun ViewManager.horizontalLayout(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: LinearLayout.() -> Unit
): LinearLayout {
    return ChaosManager.link(internalContext, theme, styledLayout, init) {
        val ll = LinearLayout(it)
        if (ll.orientation == LinearLayout.VERTICAL) ll.orientation = LinearLayout.HORIZONTAL
        return@link ll
    }.into(this)
}

// TODO: RelativeLayout
fun ViewManager.relativeLayout(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: RelativeLayout.() -> Unit
): RelativeLayout {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { RelativeLayout(it) }
        .into(this)
}

// TODO: FrameLayout
fun ViewManager.frameLayout(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: FrameLayout.() -> Unit
): FrameLayout {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { FrameLayout(it) }
        .into(this)
}

// TODO: TableLayout
fun ViewManager.tableLayout(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: TableLayout.() -> Unit
): TableLayout {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { TableLayout(it) }
        .into(this)
}

// TODO: GridLayout
fun ViewManager.gridLayout(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: GridLayout.() -> Unit
): GridLayout {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { GridLayout(it) }
        .into(this)
}

/**
 *
 *               TODO: Now for LazyViewFactory
 *
 */

// TODO: LinearLayout
fun LazyViewFactory.linearLayout(init: LinearLayout.() -> Unit) = this.add(init)

fun LazyViewFactory.verticalLayout() {
    throw UnsupportedOperationException()
}

fun LazyViewFactory.horizontalLayout() {
    throw UnsupportedOperationException()
}

// TODO: RelativeLayout
fun LazyViewFactory.relativeLayout(init: RelativeLayout.() -> Unit) = this.add(init)

// TODO: FrameLayout
fun LazyViewFactory.frameLayout(init: FrameLayout.() -> Unit) = this.add(init)

// TODO: TableLayout
fun LazyViewFactory.tableLayout(init: TableLayout.() -> Unit) = this.add(init)

// TODO: GridLayout
fun LazyViewFactory.gridLayout(init: GridLayout.() -> Unit) = this.add(init)


/**
 *
 *               TODO: Direct usage for activity
 *
 */

fun Activity.linearLayout(init: LinearLayout.() -> Unit): LinearLayout {
    val father = this.window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)
    val ll = LinearLayout(this)
    ll.init()
    if (ll.layoutParams == null) ll.layoutParams = ViewGroup.LayoutParams(-1, -1)
    father.addView(ll)
    return ll
}

fun Activity.verticalLayout(init: LinearLayout.() -> Unit): LinearLayout {
    val ll = linearLayout(init)
    if (ll.orientation == LinearLayout.HORIZONTAL) ll.orientation = LinearLayout.VERTICAL
    return ll
}

fun Activity.horizontalLayout(init: LinearLayout.() -> Unit): LinearLayout {
    val ll = linearLayout(init)
    if (ll.orientation == LinearLayout.VERTICAL) ll.orientation = LinearLayout.HORIZONTAL
    return ll
}

fun Activity.relativeLayout(init: RelativeLayout.() -> Unit): RelativeLayout {
    val father = this.window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)
    val rl = RelativeLayout(this)
    rl.init()
    if (rl.layoutParams == null) rl.layoutParams = ViewGroup.LayoutParams(-1, -1)
    father.addView(rl)
    return rl
}

fun Activity.frameLayout(init: FrameLayout.() -> Unit): FrameLayout {
    val father = this.window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)
    val fl = FrameLayout(this)
    fl.init()
    if (fl.layoutParams == null) fl.layoutParams = ViewGroup.LayoutParams(-1, -1)
    father.addView(fl)
    return fl
}

fun Activity.gridLayout(init: GridLayout.() -> Unit): GridLayout {
    val father = this.window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)
    val gl = GridLayout(this)
    gl.init()
    if (gl.layoutParams == null) gl.layoutParams = ViewGroup.LayoutParams(-1, -1)
    father.addView(gl)
    return gl
}

fun Activity.tableLayout(init: TableLayout.() -> Unit): TableLayout {
    val father = this.window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)
    val tl = TableLayout(this)
    tl.init()
    if (tl.layoutParams == null) tl.layoutParams = ViewGroup.LayoutParams(-1, -1)
    father.addView(tl)
    return tl
}

/**
 *
 *               TODO: A way to directly using third-party's layout in activity (Must extends from ViewGroup)
 *
 */

inline fun <reified T : ViewGroup> Activity.layout(init: T.() -> Unit): T {
    val father = this.window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)
    val constructor = T::class.java.getDeclaredConstructor(Context::class.java)
    constructor.isAccessible = true
    val instance = constructor.newInstance(this)
    instance.init()
    if (instance.layoutParams == null) instance.layoutParams = ViewGroup.LayoutParams(-1, -1)
    father.addView(instance)
    return instance
}