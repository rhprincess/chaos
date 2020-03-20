package io.rhprincess.chaos.styling

import android.graphics.Typeface
import android.widget.TextView

object Typography {

    object Headline1 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 96f
                letterSpacing = 0f
            }
        }
    })

    object Headline2 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 60f
                letterSpacing = 0f
            }
        }
    })

    object Headline3 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 48f
                letterSpacing = 0f
            }
        }
    })

    object Headline4 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 34f
                letterSpacing = 0.25f
            }
        }
    })

    object Headline5 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 24f
                letterSpacing = 0f
            }
        }
    })

    object Headline6 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 20f
                letterSpacing = 0.15f
            }
        }
    })

    object Subtitle1 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 16f
                letterSpacing = 0.15f
            }
        }
    })

    object Subtitle2 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 14f
                letterSpacing = 0.1f
            }
        }
    })

    object Body1 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 16f
                letterSpacing = 0.5f
            }
        }
    })

    object Body2 : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 14f
                letterSpacing = 0.25f
            }
        }
    })

    object Button : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 14f
                letterSpacing = 1.25f
            }
        }
    })

    object Caption : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 12f
                letterSpacing = 0.4f
            }
        }
    })

    @Suppress("SpellCheckingInspection")
    object OVERLINE : IStyle({
        when (it) {
            is TextView -> it.apply {
                textSize = 10f
                letterSpacing = 1.5f
            }
        }
    })

    object Bold : IStyle({
        when (it) {
            is TextView -> it.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
    })

    object Italic : IStyle({
        when (it) {
            is TextView -> it.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
        }
    })

    object BoldItalic : IStyle({
        when (it) {
            is TextView -> it.typeface = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)
        }
    })

    object Default : IStyle({
        when (it) {
            is TextView -> it.typeface = Typeface.DEFAULT
        }
    })

    object DefaultBold : IStyle({
        when (it) {
            is TextView -> it.typeface = Typeface.DEFAULT_BOLD
        }
    })

}