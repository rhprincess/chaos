@file:Suppress("unused")

package io.rhprincess.chaos.main.widgets

import android.view.View
import android.view.ViewManager
import android.webkit.WebView
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import io.rhprincess.chaos.lazy.LazyViewFactory
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
fun ViewManager.switch(
    text: CharSequence = "",
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Switch.() -> Unit = { this.text = text }
): Switch {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { Switch(it) }.into(this)
}

// TODO: ImageButton
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
fun ViewManager.view(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: View.() -> Unit = {}
): View {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { View(it) }.into(this)
}

// TODO: ImageView
fun ViewManager.imageView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ImageView.() -> Unit = {}
): ImageView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ImageView(it) }
        .into(this)
}

// TODO: WebView
fun ViewManager.webView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: WebView.() -> Unit = {}
): WebView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { WebView(it) }
        .into(this)
}

// TODO: VideoView
fun ViewManager.videoView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: VideoView.() -> Unit = {}
): VideoView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { VideoView(it) }
        .into(this)
}

// TODO: CalendarView
fun ViewManager.calendarView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: CalendarView.() -> Unit = {}
): CalendarView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { CalendarView(it) }
        .into(this)
}

// TODO: ProgressBar
fun ViewManager.progressBar(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ProgressBar.() -> Unit = {}
): ProgressBar {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ProgressBar(it) }
        .into(this)
}

// TODO: SeekBar
fun ViewManager.seekBar(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: SeekBar.() -> Unit = {}
): SeekBar {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { SeekBar(it) }
        .into(this)
}

// TODO: RatingBar
fun ViewManager.ratingBar(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: RatingBar.() -> Unit = {}
): RatingBar {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { RatingBar(it) }
        .into(this)
}

// TODO: SearchView
fun ViewManager.searchView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: SearchView.() -> Unit = {}
): SearchView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { SearchView(it) }
        .into(this)
}

// TODO: Spinner
fun ViewManager.spinner(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Spinner.() -> Unit = {}
): Spinner {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { Spinner(it) }
        .into(this)
}

// TODO: ScrollView
fun ViewManager.scrollView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ScrollView.() -> Unit = {}
): ScrollView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ScrollView(it) }
        .into(this)
}

// TODO: HorizontalScrollView
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
fun ViewManager.toolbar(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: Toolbar.() -> Unit = {}
): Toolbar {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { Toolbar(it) }
        .into(this)
}

// TODO: ListView
fun ViewManager.listView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: ListView.() -> Unit = {}
): ListView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { ListView(it) }
        .into(this)
}

// TODO: GridView
fun ViewManager.gridView(
    @StyleRes theme: Int = 0,
    @LayoutRes styledLayout: Int = 0,
    init: GridView.() -> Unit = {}
): GridView {
    return ChaosManager.link(internalContext, theme, styledLayout, init) { GridView(it) }
        .into(this)
}

/**
 *
 *               TODO: For LazyViewFactory (Lazy Injection)
 *
 */

// TODO: Button
fun LazyViewFactory.button(
    text: CharSequence = "",
    init: Button.() -> Unit = { this.text = text }
) = this.add(init)

// TODO: CheckBox
fun LazyViewFactory.checkBox(
    text: CharSequence = "",
    init: CheckBox.() -> Unit = { this.text = text }
) =
    this.add(init)

// TODO: EditText
fun LazyViewFactory.editText(init: EditText.() -> Unit) = this.add(init)

// TODO: TextView
fun LazyViewFactory.textView(
    text: CharSequence = "",
    init: TextView.() -> Unit = { this.text = text }
) = this.add(init)

// TODO: Switch
fun LazyViewFactory.switch(
    text: CharSequence = "",
    init: Switch.() -> Unit = { this.text = text }
) = this.add(init)

// TODO: ImageButton
fun LazyViewFactory.imageButton(init: ImageButton.() -> Unit) = this.add(init)

// TODO: RadioGroup
fun LazyViewFactory.radioGroup(init: RadioGroup.() -> Unit) = this.add(init)

// TODO: RadioButton
fun LazyViewFactory.radioButton(
    text: CharSequence = "",
    init: RadioButton.() -> Unit = { this.text = text }
) = this.add(init)

// TODO: ToggleButton
fun LazyViewFactory.toggleButton(
    text: CharSequence = "",
    init: ToggleButton.() -> Unit = { this.text = text }
) = this.add(init)

// TODO: View
fun LazyViewFactory.view(init: View.() -> Unit) = this.add(init)

// TODO: ImageView
fun LazyViewFactory.imageView(init: ImageView.() -> Unit) = this.add(init)

// TODO: WebView
fun LazyViewFactory.webView(init: WebView.() -> Unit) = this.add(init)

// TODO: VideoView
fun LazyViewFactory.videoView(init: VideoView.() -> Unit) = this.add(init)

// TODO: CalendarView
fun LazyViewFactory.calendarView(init: CalendarView.() -> Unit) = this.add(init)

// TODO: ProgressBar
fun LazyViewFactory.progressBar(init: ProgressBar.() -> Unit) = this.add(init)

// TODO: SeekBar
fun LazyViewFactory.seekBar(init: SeekBar.() -> Unit) = this.add(init)

// TODO: RatingBar
fun LazyViewFactory.ratingBar(init: RatingBar.() -> Unit) = this.add(init)

// TODO: SearchView
fun LazyViewFactory.searchView(init: SearchView.() -> Unit) = this.add(init)

// TODO: Spinner
fun LazyViewFactory.spinner(init: Spinner.() -> Unit) = this.add(init)

// TODO: ScrollView
fun LazyViewFactory.scrollView(init: ScrollView.() -> Unit) = this.add(init)

// TODO: HorizontalScrollView
fun LazyViewFactory.horizontalScrollView(init: HorizontalScrollView.() -> Unit) = this.add(init)

// TODO: Toolbar
fun LazyViewFactory.toolbar(init: Toolbar.() -> Unit) = this.add(init)

// TODO: ListView
fun LazyViewFactory.listView(init: ListView.() -> Unit) = this.add(init)

// TODO: GridView
fun LazyViewFactory.gridView(init: GridView.() -> Unit) = this.add(init)