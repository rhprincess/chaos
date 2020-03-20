package io.rhprincess.chaos.factory

import android.content.Context
import io.rhprincess.chaos.main.ChaosManager

@Suppress("PropertyName")
abstract class IComponent {

    abstract val layout: ChaosUI
    abstract val reusable: Boolean

    companion object {
        private var reusableUI: ChaosManager? = null
    }

    fun build(context: Context): ChaosManager {
        return if (reusableUI != null && reusable) {
            reusableUI!!
        } else {
            reusableUI = layout(context)
            reusableUI!!
        }
    }
}