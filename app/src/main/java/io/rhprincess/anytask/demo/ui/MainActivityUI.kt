package io.rhprincess.anytask.demo.ui

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.MotionEvent
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import io.rhprincess.chaos.factory.ChaosDslUI
import io.rhprincess.chaos.factory.IComponent
import io.rhprincess.chaos.main.*
import io.rhprincess.chaos.main.widgets.textView
import io.rhprincess.chaos.main.widgets.verticalLayout
import io.rhprincess.chaos.styling.Typography
import io.rhprincess.chaos.styling.styles
import io.rhprincess.chaos.support.view.divider

@SuppressLint("ClickableViewAccessibility")
class MainActivityUI : IComponent() {

    @SuppressLint("SetTextI18n")
    override var layout: ChaosDslUI = {
        it.UI {
            widget<DrawerLayout> {
                layoutParams = lparams { width = MATCH_PARENT; height = MATCH_PARENT }
                // content
                verticalLayout {
                    gravity = Gravity.CENTER
                    layoutParams = DrawerLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                        gravity = Gravity.NO_GRAVITY
                    }
                    var lastX = 0f
                    var lastY = 0f
                    textView {
                        chaosId = "content_text"
                        text = "Sample UI"
                        styles = arrayOf(Typography.Headline2, Typography.Bold)
                    }.setOnTouchListener { v, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                lastX = event.x
                                lastY = event.y
                                v.animate().scaleX(.85f).scaleY(.85f)
                            }
                            MotionEvent.ACTION_MOVE -> {
                                val offsetX = event.x - lastX
                                val offsetY = event.y - lastY
                                v.x += offsetX
                                v.y += offsetY
                            }
                            MotionEvent.ACTION_UP -> {
                                v.animate().scaleX(1f).scaleY(1f)
                            }
                        }
                        return@setOnTouchListener true
                    }

                    divider(1f, height = 2.dp, color = "#000000".color)

                    widget<MaterialButton> {
                        chaosId = "open"
                        text = "Open"
                    }.click {
                        this@widget.open()
                    }
                }

                // navigation view
                widget<NavigationView> {
                    chaosId = "navigation"
                    val dlp = DrawerLayout.LayoutParams(WRAP_PARENT, MATCH_PARENT)
                    dlp.gravity = GravityCompat.START
                    layoutParams = dlp
                    menu.apply {
                        add(ChaosId.bind("group_one"), ChaosId.bind("menu_one"), 0, "Menu One.")
                        add("group_one".toChaosId, ChaosId.bind("menu_two"), 0, "Menu Two.")
                        add(ChaosId.bind("group_two"), ChaosId.bind("menu_three"), 0, "Menu Three.")
                        add("group_two".toChaosId, ChaosId.bind("menu_four"), 0, "Menu Four.")
                        add(ChaosId.bind("group_three"), ChaosId.bind("menu_five"), 0, "Menu Five.")
                        add("group_three".toChaosId, ChaosId.bind("menu_six"), 0, "Menu Six.")
                    }
                    setNavigationItemSelectedListener {
                        when (it.itemId) {
                            "menu_one".toChaosId -> "One".toast()
                            "menu_two".toChaosId -> "Two".toast()
                        }
                        return@setNavigationItemSelectedListener true
                    }
                }
            }
        }
    }

}