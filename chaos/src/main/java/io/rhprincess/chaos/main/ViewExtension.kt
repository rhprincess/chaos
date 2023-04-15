package io.rhprincess.chaos.main

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.*

fun <T : View> views(vararg v: T, block: T.() -> Unit) {
    for (t in v) {
        block(t)
    }
}

fun <T : View> T.click(block: T.() -> Unit) {
    this.setOnClickListener { block() }
}

fun <T : View> T.longClick(block: T.() -> Boolean) {
    this.setOnLongClickListener { block() }
}

class ViewGesture(private val view: View) {

    private var showPressed: (e: MotionEvent) -> Unit = {}
    private var singleTapUpend: (e: MotionEvent) -> Boolean = { false }
    private var downed: (e: MotionEvent) -> Boolean = { false }
    private var flanged: (
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ) -> Boolean = { _, _, _, _ -> false }
    private var scrolled: (
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) -> Boolean = { _, _, _, _ -> false }
    private var longPressed: (e: MotionEvent) -> Unit = {}
    var doubleClicked: (e: MotionEvent) -> Unit = {}
    var doubleClickDelay = 300L

    val gestureDetector = object : GestureDetector.OnGestureListener {
        override fun onShowPress(e: MotionEvent) {
            showPressed(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return singleTapUpend(e)
        }

        override fun onDown(e: MotionEvent): Boolean {
            return downed(e)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return flanged(e1, e2, velocityX, velocityY)
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return scrolled(e1, e2, distanceX, distanceY)
        }

        override fun onLongPress(e: MotionEvent) {
            longPressed(e)
        }
    }

    fun showPress(pressed: (e: MotionEvent) -> Unit) {
        this.showPressed = pressed
    }

    fun singleTapUp(tap: (e: MotionEvent) -> Boolean) {
        this.singleTapUpend = tap
    }

    fun down(downed: (e: MotionEvent) -> Boolean) {
        this.downed = downed
    }

    fun fling(
        flanged: (
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ) -> Boolean
    ) {
        this.flanged = flanged
    }

    fun scroll(
        scrolled: (
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ) -> Boolean
    ) {
        this.scrolled = scrolled
    }

    fun longPress(longPressed: (e: MotionEvent) -> Unit) {
        this.longPressed = longPressed
    }

    fun doubleClick(clicked: (e: MotionEvent) -> Unit) {
        this.doubleClicked = clicked
    }

    fun doubleClick(delayedMillis: Long, clicked: (e: MotionEvent) -> Unit) {
        this.doubleClickDelay = delayedMillis
        this.doubleClicked = clicked
    }
}

@SuppressLint("ClickableViewAccessibility")
fun <T : View> T.gesture(init: ViewGesture.() -> Unit) {
    val wrap = ViewGesture(this)
    wrap.init()
    val detector = GestureDetector(this.context, wrap.gestureDetector)
    var time = System.currentTimeMillis()
    this.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (System.currentTimeMillis() - time <= wrap.doubleClickDelay) wrap.doubleClicked(
                    event
                )
                time = System.currentTimeMillis()
            }
        }
        return@setOnTouchListener detector.onTouchEvent(event)
    }
}

@Suppress("unused")
fun FrameLayout.lparams(init: FrameLayout.LayoutParams.() -> Unit): FrameLayout.LayoutParams {
    val wrap = FrameLayout.LayoutParams(-2, -2)
    wrap.init()
    return wrap
}

@Suppress("unused")
fun LinearLayout.lparams(init: LinearLayout.LayoutParams.() -> Unit): LinearLayout.LayoutParams {
    val wrap = LinearLayout.LayoutParams(-2, -2)
    wrap.init()
    return wrap
}

@Suppress("unused")
fun RelativeLayout.lparams(init: RelativeLayout.LayoutParams.() -> Unit): RelativeLayout.LayoutParams {
    val wrap = RelativeLayout.LayoutParams(-2, -2)
    wrap.init()
    return wrap
}

@Suppress("unused")
fun GridLayout.lparams(init: GridLayout.LayoutParams.() -> Unit): GridLayout.LayoutParams {
    val wrap = GridLayout.LayoutParams()
    wrap.init()
    return wrap
}

@Suppress("unused")
fun TableLayout.lparams(init: TableLayout.LayoutParams.() -> Unit): TableLayout.LayoutParams {
    val wrap = TableLayout.LayoutParams(-2, -2)
    wrap.init()
    return wrap
}