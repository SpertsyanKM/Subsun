package ru.lefty.subsun.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color

val SubsunTypography = Typography(
    FontFamily.SansSerif,
    h1 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 96.sp
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 60.sp
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 48.sp
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 34.sp
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 20.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Gray
    ),
)