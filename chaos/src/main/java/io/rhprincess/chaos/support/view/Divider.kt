package io.rhprincess.chaos.support.view

import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import io.rhprincess.chaos.main.dp
import io.rhprincess.chaos.main.internalContext

// Divider has no return type.
fun ViewManager.divider(
    alpha: Float = 1f,
    height: Int = 1.dp,
    margin: Int = 0,
    leftMargin: Int = 0,
    topMargin: Int = 0,
    rightMargin: Int = 0,
    bottomMargin: Int = 0,
    color: Int = 0x20000000
) {
    val dividerView = View(internalContext)
    dividerView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    dividerView.setBackgroundColor(color)
    dividerView.alpha = alpha
    val marginLps = ViewGroup.MarginLayoutParams(dividerView.layoutParams)
    marginLps.leftMargin = leftMargin
    marginLps.topMargin = topMargin
    marginLps.bottomMargin = bottomMargin
    marginLps.rightMargin = rightMargin
    if (margin != 0) {
        marginLps.leftMargin = margin
        marginLps.topMargin = margin
        marginLps.bottomMargin = margin
        marginLps.rightMargin = margin
    }
    addView(dividerView, marginLps)
}