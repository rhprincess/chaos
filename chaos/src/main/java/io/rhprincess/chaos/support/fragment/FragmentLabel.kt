package io.rhprincess.chaos.support.fragment

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.rhprincess.chaos.factory.ChaosIdName
import io.rhprincess.chaos.main.ChaosId
import io.rhprincess.chaos.main.toChaosId

class FragmentLabel : FrameLayout.LayoutParams(-1, -1) {
    var fragmentId: ChaosIdName = ""
        set(value) {
            field = value
            if (!ChaosId.isExists(value)) {
                ChaosId.bind(value)
            }
        }
    var name: String = ""
}

/**
 * 与xml里的fragment标签相似的fragment标签
 *
 * 注意: 必须给fragment标签的父布局设置一个ID或者给Fragment设置ID，否则报错
 *
 * 在Activity中获取Fragment:
 *
 * 如果给父控件设置了ID并且fragmentId为空：supportFragmentManager.findFragmentById(父控件ID)
 *
 * 如果给父控件设置了ID并且fragmentId不为空：supportFragmentManager.findFragmentByTag(fragmentId)
 *
 * 没有给父控件设置ID但fragmentId不为空：supportFragmentManager.findFragmentById(fragmentId.toChaosId)
 */
fun FrameLayout.fragment(
    supportFragmentManager: FragmentManager,
    init: FragmentLabel.() -> Unit
): Fragment {
    val wrapper = FragmentLabel()
    wrapper.init()
    val fragment = Class.forName(wrapper.name).newInstance() as Fragment
    if (this.id == View.NO_ID) {
        this.id = wrapper.fragmentId.toChaosId
        if (wrapper.fragmentId.isNotEmpty()) {
            supportFragmentManager.beginTransaction().add(wrapper.fragmentId.toChaosId, fragment)
                .commit()
        } else {
            throw RuntimeException("Stub! You must set an id for this fragment's container or set \"fragmentId\" for this fragment")
        }
    } else {
        if (wrapper.fragmentId.isNotEmpty()) {
            supportFragmentManager.beginTransaction().add(this.id, fragment, wrapper.fragmentId)
                .commit()
        } else {
            supportFragmentManager.beginTransaction().add(this.id, fragment)
                .commit()
        }
    }
    fragment.view?.layoutParams = wrapper
    return fragment
}