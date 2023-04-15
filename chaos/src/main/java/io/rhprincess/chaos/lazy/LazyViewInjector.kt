package io.rhprincess.chaos.lazy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import io.rhprincess.chaos.main.ChaosException
import io.rhprincess.chaos.main.ChaosManager

/**
 *
 *     注意:
 *     懒加载注入器并不支持使用include等动态注入的方法，以及样式化的布局也并不支持
 *     未来可能会对include以及样式化布局做出相关适配，但目前并不支持
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

    /**
     * 将布局注入，并回收实例
     */
    fun inject() {
        if (isInjected) throw RuntimeException("Stub! Lazy init block cannot be injected twice.")
        if (factory != null) {
            factory!!.fetch().forEach {
                targetParent.addView(it, replacePosition)
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

class LazyViewFactory(val context: Context) {
    val lazyViews = ArrayList<View>()
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

    inline fun <reified T : View> add(
        @StyleRes theme: Int = 0,
        @LayoutRes styledLayout: Int = 0,
        noinline initial: T.() -> Unit,
        initialView: (wrappedContext: Context) -> T? = { null }
    ): T {
        val view = ChaosManager.init(context, theme, styledLayout, initial, initialView)
        lazyViews.add(view)
        return view
    }

    fun fetch(): ArrayList<View> = lazyViews
}

/**
 * 用该方法可以声明懒加载布局，使用该方法并不会造成嵌套，而是会注入到指定的位置
 */
fun ViewGroup.lazyInject(init: LazyViewFactory.() -> Unit): LazyViewInjector {
    val factory = LazyViewFactory(context)
    factory.init()
    val injector = LazyViewInjector(this, factory)
    if (factory.id.isNotEmpty()) LazyViewFactory.put(factory.id, injector)
    return injector
}

/**
 * 懒加载Xml文件
 */
fun ViewGroup.lazyInject(@LayoutRes layout: Int): LazyViewInjector {
    return LazyViewInjector(this, layout)
}