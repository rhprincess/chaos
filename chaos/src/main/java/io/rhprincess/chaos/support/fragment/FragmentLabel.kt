package io.rhprincess.chaos.support.fragment

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.rhprincess.chaos.factory.ChaosIdName
import io.rhprincess.chaos.main.ChaosId
import io.rhprincess.chaos.main.toChaosId

class FragmentLabel {
    var fragmentId: ChaosIdName = ""
        set(value) {
            field = value
            if (!ChaosId.isExists(value)) {
                ChaosId.bind(value)
            }
        }
    var name: String = ""
    var width: Int = -1
    var height: Int = -1
}

class FragmentLabel2 {
    var fragmentId: ChaosIdName = ""
        set(value) {
            field = value
            if (!ChaosId.isExists(value)) {
                ChaosId.bind(value)
            }
        }
    var width: Int = -1
    var height: Int = -1
}

fun FrameLayout.fragment(
    supportFragmentManager: FragmentManager,
    init: FragmentLabel.() -> Unit
): Fragment {
    val wrapper = FragmentLabel()
    wrapper.init()
    this.id = wrapper.fragmentId.toChaosId
    val fragment = Class.forName(wrapper.name).newInstance() as Fragment
    supportFragmentManager.beginTransaction().add(wrapper.fragmentId.toChaosId, fragment).commit()
    fragment.view?.layoutParams = FrameLayout.LayoutParams(
        wrapper.width,
        wrapper.height
    )
    return fragment
}

inline fun <reified T : Fragment> FrameLayout.fragment(
    supportFragmentManager: FragmentManager,
    init: FragmentLabel2.() -> Unit,
    fId: String = ""
): T {
    val wrapper = FragmentLabel2()
    wrapper.fragmentId = fId
    wrapper.init()
    this.id = wrapper.fragmentId.toChaosId
    val fragment = T::class.java.getDeclaredConstructor().newInstance()
    supportFragmentManager.beginTransaction().add(wrapper.fragmentId.toChaosId, fragment).commit()
    fragment.view?.layoutParams = FrameLayout.LayoutParams(
        wrapper.width,
        wrapper.height
    )
    return fragment
}