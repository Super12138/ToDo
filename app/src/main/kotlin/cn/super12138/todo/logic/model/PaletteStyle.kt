package cn.super12138.todo.logic.model

import androidx.annotation.StringRes
import cn.super12138.todo.R

enum class PaletteStyle(
    val id: Int,
    @StringRes val nameRes: Int
) {
    TonalSpot(id = 1, nameRes = R.string.palette_tonal_spot),
    Neutral(id = 2, nameRes = R.string.palette_neutral),
    Vibrant(id = 3, nameRes = R.string.palette_vibrant),
    Expressive(id = 4, nameRes = R.string.palette_expressive),
    Rainbow(id = 5, nameRes = R.string.palette_rainbow),
    FruitSalad(id = 6, nameRes = R.string.palette_fruit_salad),
    Monochrome(id = 7, nameRes = R.string.palette_monochrome),
    Fidelity(id = 8, nameRes = R.string.palette_fidelity),
    Content(id = 9, nameRes = R.string.palette_content);

    companion object {
        fun fromId(id: Int) = entries.firstOrNull { it.id == id } ?: TonalSpot
    }
}