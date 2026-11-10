package com.quedadapp.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.quedadapp.app.R

// DM Sans es una fuente variable (un único archivo con eje de peso),
// así que cada peso se saca pidiendo esos "ajustes de variación" al
// mismo recurso, en vez de tener un .ttf distinto por peso.
private fun dmSans(weight: Int) = Font(
    resId = R.font.dm_sans_variable,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight)),
)

val DmSans = FontFamily(
    dmSans(400),
    dmSans(500),
    dmSans(700),
)

// Tamaños orientativos de docs/diseno.md: cuerpo 14–16px, subtítulos
// 24px, títulos 38–48px, con interletraje negativo en los grandes.
val Typography = Typography(
    bodyLarge = TextStyle(fontFamily = DmSans, fontWeight = FontWeight(400), fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = DmSans, fontWeight = FontWeight(400), fontSize = 14.sp),
    labelLarge = TextStyle(fontFamily = DmSans, fontWeight = FontWeight(500), fontSize = 14.sp),
    titleLarge = TextStyle(
        fontFamily = DmSans,
        fontWeight = FontWeight(700),
        fontSize = 24.sp,
        letterSpacing = (-0.3).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = DmSans,
        fontWeight = FontWeight(700),
        fontSize = 38.sp,
        letterSpacing = (-0.8).sp,
    ),
)
