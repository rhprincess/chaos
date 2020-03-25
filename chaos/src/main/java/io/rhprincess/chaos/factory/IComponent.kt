package io.rhprincess.chaos.factory

import android.content.Context
import io.rhprincess.chaos.main.ChaosManager

/**
 * @author PRINCE$$
 * 抽象类：IComponent（组件），需被继承
 */
@Suppress("PropertyName")
abstract class IComponent {

    /**
     * 该变量为该组件的布局
     */
    abstract val layout: ChaosUI

    /**
     * 该组件的布局是否可以被复用
     */
    abstract val reusable: Boolean

    companion object {
        private var reusableUI: ChaosManager? = null
    }

    /**
     * @param context 传入的上下文
     * @return 返回 ChaosManager
     */
    fun build(context: Context): ChaosManager {
        return if (reusableUI != null && reusable) {
            reusableUI!!
        } else {
            reusableUI = layout(context)
            reusableUI!!
        }
    }
}