package io.rhprincess.anytask.demo.ui

import android.widget.Button
import io.rhprincess.chaos.factory.IComponent
import io.rhprincess.chaos.main.UI
import io.rhprincess.chaos.main.chaosId
import io.rhprincess.chaos.main.widgets.button

class TestUI : IComponent({
    it.UI(autoAttach = false) {
        button("你好") {
            chaosId = "binding_button"
        }
    }
}) {
    val btn: () -> Button = { "binding_button".bindAtComponent() }
}