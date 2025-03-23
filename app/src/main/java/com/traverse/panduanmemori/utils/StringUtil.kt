package com.traverse.panduanmemori.utils

import androidx.compose.ui.graphics.Color

object StringUtil {
    fun getRandomColorFromString(input: String): Color {
        val hash = input.hashCode()
        val r = (hash and 0xFF0000 shr 16) and 0xFF
        val g = (hash and 0x00FF00 shr 8) and 0xFF
        val b = (hash and 0x0000FF) and 0xFF

        return Color(r, g, b)
    }
}
