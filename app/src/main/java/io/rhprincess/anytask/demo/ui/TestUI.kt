package io.rhprincess.anytask.demo.ui

import android.graphics.Color
import android.view.ViewManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.google.android.material.button.MaterialButton
import io.rhprincess.chaos.annotations.LazyInject
import io.rhprincess.chaos.factory.ChaosDslUI
import io.rhprincess.chaos.factory.IComponent
import io.rhprincess.chaos.lazy.lazyInject
import io.rhprincess.chaos.main.*
import io.rhprincess.chaos.main.widgets.button
import io.rhprincess.chaos.main.widgets.verticalLayout

class TestUI : IComponent() {

    override var layout: ChaosDslUI = {
        it.UI {
            verticalLayout {
                button("懒加载块之前的按钮")

                val injector = lazyInject {
                    button("懒加载按钮 1")
                    button("懒加载按钮 2")
                    button("懒加载按钮 3")
                    button("懒加载按钮 4")
                    button("懒加载按钮 5")
                    button("懒加载按钮 6")

                    materialButton("Generate by LazyInjector", Color.WHITE) {}
                }

                button("懒加载块之后的按钮")
                button("加载").click {
                    if (!injector.isInjected) injector.inject()
                    else toast("You have already injected this layout", Toast.LENGTH_SHORT)
                }
            }
        }
    }

    @LazyInject("com.google.android.material.button")
    fun ViewManager.materialButton(
        text: String,
        textColor: Int,
        @StyleRes theme: Int = 0,
        @LayoutRes styledLayout: Int = 0,
        init: MaterialButton.() -> Unit = {}
    ): MaterialButton {
        return ChaosManager.link(internalContext, theme, styledLayout, init) {
            MaterialButton(it)
        }.into(this)
    }

}