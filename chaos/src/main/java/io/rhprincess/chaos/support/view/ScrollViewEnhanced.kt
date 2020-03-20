package io.rhprincess.chaos.support.view

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import io.rhprincess.chaos.main.internalContext

// TODO: ScrollView Enhanced

inline fun <reified Container : ViewGroup> ViewManager.scrollView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Container.(scrollView: ScrollView) -> Unit = {}
): ScrollView {
    val themedContext =
        if (theme != 0) ContextThemeWrapper(internalContext, theme) else internalContext
    val sV = if (styledLayout == 0) {
        ScrollView(themedContext)
    } else {
        LayoutInflater.from(themedContext).inflate(styledLayout, null) as ScrollView
    }
    val constructor = Container::class.java.getDeclaredConstructor(Context::class.java)
    constructor.isAccessible = true
    val container = constructor.newInstance(themedContext)
    container.init(sV)
    if (container.layoutParams != null) {
        sV.addView(container)
    } else {
        sV.addView(container, ViewGroup.LayoutParams(-1, 1))
    }
    if (sV.layoutParams != null) {
        this.addView(sV, sV.layoutParams)
    } else {
        this.addView(sV, ViewGroup.LayoutParams(-2, -2))
    }
    return sV
}

inline fun <reified Container : ViewGroup> ViewManager.horizontalScrollView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Container.(scrollView: HorizontalScrollView) -> Unit = {}
): HorizontalScrollView {
    val themedContext =
        if (theme != 0) ContextThemeWrapper(internalContext, theme) else internalContext
    val sV = if (styledLayout == 0) {
        HorizontalScrollView(themedContext)
    } else {
        LayoutInflater.from(themedContext).inflate(styledLayout, null) as HorizontalScrollView
    }
    val constructor = Container::class.java.getDeclaredConstructor(Context::class.java)
    constructor.isAccessible = true
    val container = constructor.newInstance(themedContext)
    container.init(sV)
    if (container.layoutParams != null) {
        sV.addView(container)
    } else {
        sV.addView(container, ViewGroup.LayoutParams(-1, -1))
    }
    if (sV.layoutParams != null) {
        this.addView(sV, sV.layoutParams)
    } else {
        this.addView(sV, ViewGroup.LayoutParams(-2, -2))
    }
    return sV
}