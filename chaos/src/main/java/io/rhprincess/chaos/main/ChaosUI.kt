package io.rhprincess.chaos.main

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import io.rhprincess.chaos.R
import io.rhprincess.chaos.factory.IComponent

class ChaosUI : FrameLayout {

    constructor(context: Context) : super(context) {
        throw UnsupportedOperationException("Stub! ChaosUI must be use in xml")
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initial(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initial(context, attrs)
    }

    @Suppress("unused")
    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initial(context, attrs)
    }

    private fun initial(context: Context, attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ChaosUI)
        val merge = array.getBoolean(R.styleable.ChaosUI_merge, false)
        val componentClassStr = array.getString(R.styleable.ChaosUI_component)
        val component = if (componentClassStr != null && componentClassStr.isNotEmpty()) {
            Class.forName(componentClassStr).newInstance() as IComponent
        } else throw ChaosException.ComponentNotFound("Cannot found Chaos IComponent class: ${componentClassStr ?: "none"}")
        array.recycle()
        addView(component.build(context).view)
    }

}