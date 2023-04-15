@file:Suppress("unused")

package io.rhprincess.chaos.main.widgets

import android.view.View
import android.view.ViewManager
import android.webkit.WebView
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import io.rhprincess.chaos.annotations.LazyInject
import io.rhprincess.chaos.main.ChaosManager
import io.rhprincess.chaos.main.internalContext

/**
 *
 *               TODO: For ViewManager
 *
 */

// TODO: Button
/**
 *  Another simple usage for button
 *  button("Simple Button")
 */
@LazyInject("android.widget")
fun ViewManager.button(
    text: CharSequence = "",
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Button.() -> Unit = { this.text = text }
): Button {
    return ChaosManager.link(internalContext, theme, styledLayout, init) {
        Button(it)
    }.into(this)
}

// TODO: CheckBox
@LazyInject("android.widget")
fun ViewManager.checkBox(
    text: CharSequence = "",
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: CheckBox.() -> Unit = { this.text = text }
): CheckBox {
    return ChaosManager.link(internalContext, theme, styledLayout, init) {
        CheckBox(it)
    }.into(this)
}

// TODO: EditText
@LazyInject("android.widget")
fun ViewManager.editText(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: EditText.() -> Unit = {}
): EditText {
    return ChaosManager.link(internalContext, theme, styledLayout, init) {
        EditText(it)
    }.into(this)
}

// TODO: TextView
@LazyInject("android.widget")
fun ViewManager.textView(
    text: CharSequence = "",
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: TextView.() -> Unit = { this.text = text }
): TextView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { TextView(it) }
        .into(this)
}

// TODO: Switch
@LazyInject("android.widget")
fun ViewManager.switch(
    text: CharSequence = "",
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Switch.() -> Unit = { this.text = text }
): Switch {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { Switch(it) }.into(this)
}

// TODO: ImageButton
@LazyInject("android.widget")
fun ViewManager.imageButton(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ImageButton.() -> Unit = {}
): ImageButton {
    return ChaosManager.link(internalContext, theme, styledLayout, init) {
        ImageButton(it)

    }.into(this)
}

// TODO: RadioGroup
@LazyInject("android.widget")
fun ViewManager.radioGroup(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: RadioGroup.() -> Unit = {}
): RadioGroup {
    return ChaosManager.link(internalContext, theme, styledLayout, init) {
        RadioGroup(it)

    }.into(this)
}

// TODO: RadioButton
@LazyInject("android.widget")
fun ViewManager.radioButton(
    text: CharSequence = "",
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: RadioButton.() -> Unit = { this.text = text }
): RadioButton {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { RadioButton(it) }
        .into(this)
}

// TODO: ToggleButton
@LazyInject("android.widget")
fun ViewManager.toggleButton(
    text: CharSequence = "",
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ToggleButton.() -> Unit = { this.text = text }
): ToggleButton {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ToggleButton(it) }
        .into(this)
}

// TODO: View
@LazyInject("android.view")
fun ViewManager.view(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: View.() -> Unit = {}
): View {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { View(it) }.into(this)
}

// TODO: ImageView
@LazyInject("android.widget")
fun ViewManager.imageView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ImageView.() -> Unit = {}
): ImageView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ImageView(it) }
        .into(this)
}

// TODO: WebView
@LazyInject("android.webkit")
fun ViewManager.webView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: WebView.() -> Unit = {}
): WebView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { WebView(it) }
        .into(this)
}

// TODO: VideoView
@LazyInject("android.widget")
fun ViewManager.videoView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: VideoView.() -> Unit = {}
): VideoView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { VideoView(it) }
        .into(this)
}

// TODO: CalendarView
@LazyInject("android.widget")
fun ViewManager.calendarView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: CalendarView.() -> Unit = {}
): CalendarView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { CalendarView(it) }
        .into(this)
}

// TODO: ProgressBar
@LazyInject("android.widget")
fun ViewManager.progressBar(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ProgressBar.() -> Unit = {}
): ProgressBar {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ProgressBar(it) }
        .into(this)
}

// TODO: SeekBar
@LazyInject("android.widget")
fun ViewManager.seekBar(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: SeekBar.() -> Unit = {}
): SeekBar {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { SeekBar(it) }
        .into(this)
}

// TODO: RatingBar
@LazyInject("android.widget")
fun ViewManager.ratingBar(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: RatingBar.() -> Unit = {}
): RatingBar {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { RatingBar(it) }
        .into(this)
}

// TODO: SearchView
@LazyInject("android.widget")
fun ViewManager.searchView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: SearchView.() -> Unit = {}
): SearchView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { SearchView(it) }
        .into(this)
}

// TODO: Spinner
@LazyInject("android.widget")
fun ViewManager.spinner(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Spinner.() -> Unit = {}
): Spinner {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { Spinner(it) }
        .into(this)
}

// TODO: ScrollView
@LazyInject("android.widget")
fun ViewManager.scrollView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ScrollView.() -> Unit = {}
): ScrollView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ScrollView(it) }
        .into(this)
}

// TODO: HorizontalScrollView
@LazyInject("android.widget")
fun ViewManager.horizontalScrollView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: HorizontalScrollView.() -> Unit = {}
): HorizontalScrollView {
    return ChaosManager.link(
        internalContext,
        theme,
        styledLayout,
        init
    ) { HorizontalScrollView(it) }.into(this)
}

// TODO: Toolbar
@LazyInject("android.widget")
fun ViewManager.toolbar(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Toolbar.() -> Unit = {}
): Toolbar {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { Toolbar(it) }
        .into(this)
}

// TODO: ListView
@LazyInject("android.widget")
fun ViewManager.listView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ListView.() -> Unit = {}
): ListView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ListView(it) }
        .into(this)
}

// TODO: GridView
@LazyInject("android.widget")
fun ViewManager.gridView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: GridView.() -> Unit = {}
): GridView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { GridView(it) }
        .into(this)
}