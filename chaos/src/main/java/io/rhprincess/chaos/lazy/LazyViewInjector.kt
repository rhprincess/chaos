package io.rhprincess.chaos.lazy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import io.rhprincess.chaos.main.ChaosException
import java.lang.reflect.Constructor

/**
 *
 *     Attention:
 *     LazyViewInjector did not support some function in AnyUI.
 *     such as 'include', styled layout, etc.
 *
 */

@Suppress("MemberVisibilityCanBePrivate")
class LazyViewInjector {

    private var targetParent: ViewGroup
    private var factory: LazyViewFactory? = null
    private var layoutId = 0
    var isInjected = false
    private var replacePosition: Int = 0

    constructor(targetParent: ViewGroup, factory: LazyViewFactory) {
        this.targetParent = targetParent
        this.factory = factory
        replacePosition = targetParent.childCount // Get holder's position in target parent.
    }

    constructor(targetParent: ViewGroup, layoutId: Int) {
        this.targetParent = targetParent
        this.layoutId = layoutId
        replacePosition = targetParent.childCount // Get holder's position in target parent.
    }

    fun inject() {
        if (isInjected) throw RuntimeException("Stub! Lazy init block cannot be injected twice.")
        if (factory != null) {
            factory!!.fetch().forEach {
                val view = it.initialize(targetParent.context)
                targetParent.addView(view, replacePosition)
                replacePosition++
            }
            factory!!.lazyViews.clear()
            factory = null
            System.gc()
        } else if (layoutId != 0) {
            val view = LayoutInflater.from(targetParent.context).inflate(layoutId, null)
            targetParent.addView(view, replacePosition)
        }
        isInjected = true
    }
}

class LazyViewFactory {
    val lazyViews = ArrayList<LazyBlock<*>>()
    var id: String = ""

    companion object {
        private val factoryMap = HashMap<String, LazyViewInjector>()
        fun put(idName: String, injector: LazyViewInjector) {
            factoryMap[idName] = injector
        }

        fun pick(idName: String): LazyViewInjector {
            return factoryMap[idName] ?: throw ChaosException.IdNotFound(idName)
        }
    }

    class LazyBlock<T : View>(
        private val constructor: Constructor<T>,
        private val initial: T.() -> Unit
    ) {
        fun initialize(context: Context): View {
            constructor.isAccessible = true
            val instance = constructor.newInstance(context)
            instance.initial()
            return instance
        }
    }

    inline fun <reified T : View> add(noinline init: T.() -> Unit) {
        val constructor = T::class.java.getDeclaredConstructor(Context::class.java)
        lazyViews.add(LazyBlock(constructor, init))
    }

    inline fun <reified T : View> widget(noinline init: T.() -> Unit) {
        val constructor = T::class.java.getDeclaredConstructor(Context::class.java)
        lazyViews.add(LazyBlock(constructor, init))
    }

    fun fetch(): ArrayList<LazyBlock<*>> = lazyViews
}

fun ViewGroup.lazyInject(init: LazyViewFactory.() -> Unit): LazyViewInjector {
    val factory = LazyViewFactory()
    factory.init()
    val injector = LazyViewInjector(this, factory)
    if (factory.id.isNotEmpty()) LazyViewFactory.put(factory.id, injector)
    return injector
}

fun ViewGroup.lazyInject(@LayoutRes layout: Int): LazyViewInjector {
    return LazyViewInjector(this, layout)
}