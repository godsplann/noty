package com.cker.noty.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.cker.noty.ui.theme.Background.blue
import com.cker.noty.ui.theme.Background.lightBlue

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

object Background {
    val lightBlue = Color(0xFFEDF9FF)
    val blue = Color(0xFFD3EFFE)
    val deepBlue = Color(0xFF133E94)
    val darkBlue = Color(0xFF0A2360)
    val white = Color(0xFFFFFFFF)
    val lightRed = Color(0x1FF0405B)
    val green = Color(0xFF35D6A5)
    val orange = Color(0x33D77C4D)
    val orangePale = Color(0x52D77C4D)
}

object Accent {
    val red = Color(0xFFF0405B)
    val deepGreen = Color(0xFF0A9A6F)
    val green = Color(0xFF35D6A5)
}

object Primary {
    val black = Color(0xFF031223)
    val mainBlue = Color(0xFF133E94)
    val lightBlue = Color(0xFF58AFEB)
}

object Secondary {
    val links = Color(0xFF328ABE)
    val softBlue = Color(0xFF439BD8)
    val grayMedium = Color(0xFF6A8295)
    val mediumBlue = Color(0xFFC7E7F9)
    val orangeMedium = Color(0xFFD77C4D)
}

object Divider {
    val gray20 = Color(0x336A8295)
    val gray12 = Color(0x1F6A8295)
    val gray16 = Color(0x6A829529)
}

val bgGradient = Brush.verticalGradient(
    0.2f to lightBlue,
    1f to blue,
)

val blackFadeGradient = Brush.verticalGradient(
    0f to Color(0x00031223),
    1f to Color(0x8F031223),
)

val blackFadeGradientReversed = Brush.verticalGradient(
    0f to Color(0x3D031223),
    1f to Color(0x00031223),
)

val deepBlueToDarkGradient = Brush.horizontalGradient(
    0f to Background.deepBlue,
    1f to Background.darkBlue,
)