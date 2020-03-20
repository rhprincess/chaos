package io.rhprincess.chaos.factory

import android.content.Context
import io.rhprincess.chaos.main.ChaosException
import io.rhprincess.chaos.main.ChaosManager

inline fun <reified UI : IComponent> createUI(context: Context, vararg params: Any): ChaosManager {
    val constructors = UI::class.java.declaredConstructors
    return when {
        constructors.size == 1 -> { // One Constructor
            val constructor = constructors[0]
            constructor.isAccessible = true
            if (params.isEmpty())
                constructor.newInstance() as UI
            else
                constructor.newInstance(params) as UI
        }
        constructors.size > 1 -> {
            throw ChaosException.MultiConstructorFailure(1, constructors.size)
        }
        else -> {
            throw ChaosException.ConstructorNotFound(UI::class.java.canonicalName!!)
        }
    }.build(context)
}