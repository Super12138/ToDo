package cn.super12138.todo.ui.theme

import android.content.Context
import cn.super12138.todo.R

enum class PaletteStyle(val id: Int) {
    TonalSpot(1),
    Neutral(2),
    Vibrant(3),
    Expressive(4),
    Rainbow(5),
    FruitSalad(6),
    Monochrome(7),
    Fidelity(8),
    Content(9);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            TonalSpot -> R.string.palette_tonal_spot
            Neutral -> R.string.palette_neutral
            Vibrant -> R.string.palette_vibrant
            Expressive -> R.string.palette_expressive
            Rainbow -> R.string.palette_rainbow
            FruitSalad -> R.string.palette_fruit_salad
            Monochrome -> R.string.palette_monochrome
            Fidelity -> R.string.palette_fidelity
            Content -> R.string.palette_content
        }
        return context.getString(resId)
    }

    companion object {
        fun fromId(id: Int) = entries.firstOrNull { it.id == id } ?: TonalSpot
    }
}