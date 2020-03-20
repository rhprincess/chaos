package io.rhprincess.chaos.styling

import android.view.View
import android.view.ViewGroup

/**
 * Examples:
 *
 *
 * when our style no need any constructor params,
 * we can using object keyword to modify our Style,
 * and we can directly use it without to initial it.
 *
 *object HeadLineStyle : IStyle({
 *    when (it) {
 *        is TextView -> it.apply {
 *            textSize = 35f
 *            textColor = "#000000".color
 *        }
 *    }
 *})
 *
 * textView {
 *      style = HeadLineStyle
 * }
 */

@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class IStyle(val styling: (view: View) -> Unit) {
    var parent: ViewGroup? = null

    constructor(parent: ViewGroup? = null, styling: (view: View) -> Unit) : this(styling) {
        this.parent = parent
    }
}

// Note: The next style will override the previous style.
fun <T : View> T.style(vararg iStyles: IStyle) {
    iStyles.forEach {
        it.styling(this)
    }
}

var <T : View> T.style: IStyle?
    get() = null
    set(value) {
        value!!.styling(this)
    }

var <T : View> T.styles: Array<IStyle>?
    get() = null
    set(value) {
        value!!.forEach {
            it.styling(this)
        }
    }