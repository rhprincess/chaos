package io.rhprincess.chaos.factory

import android.content.Context
import io.rhprincess.chaos.main.ChaosManager

/**
 * @author PRINCE$$
 * 抽象类：IComponent（组件），需被继承
 */
abstract class IComponent(val layout: ChaosUI) {

    /**
     * @param context 传入的上下文
     * @return 返回 ChaosManager
     */
    fun build(context: Context): ChaosManager {
        return layout(context)
    }
}