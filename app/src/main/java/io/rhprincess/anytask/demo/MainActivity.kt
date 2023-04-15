package io.rhprincess.anytask.demo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.rhprincess.anytask.demo.ui.MainActivityUI

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().build(this)
    }

}
