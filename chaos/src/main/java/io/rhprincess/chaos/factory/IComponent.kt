package io.rhprincess.chaos.factory

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import io.rhprincess.chaos.main.ChaosManager
import io.rhprincess.chaos.main.toChaosId

/**
 * @author PRINCE$$
 * 抽象类：IComponent（组件），需被继承
 */
abstract class IComponent : ViewManager {

    private lateinit var view: View
    abstract var layout: ChaosDslUI

    /**
     * @param context 传入的上下文
     * @return 返回 ChaosManager
     */
    fun build(context: Context): ChaosManager {
        val lay = layout(context)
        this.view = lay.view
        return lay
    }

    fun <T : View> String.bindAtComponent(): T {
        return view.findViewById(this.toChaosId)
    }

    override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
        // Did not support adding view.
    }

    override fun removeView(child: View?) {
        (view as ViewGroup).removeView(child)
    }

    override fun updateViewLayout(view: View?, params: ViewGroup.LayoutParams?) {
        throw UnsupportedOperationException("Stub!")
    }
}