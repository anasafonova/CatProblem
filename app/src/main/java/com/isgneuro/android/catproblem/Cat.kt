package com.isgneuro.android.catproblem

import java.util.*

data class Cat(
    val id: UUID = UUID.randomUUID(),
    val catName: String = "",
    val catCity: String = "",
    val catColor: String = "",
    val catEyeColor: String = "",
    val catSize: String = "",
    val isHomeless: Boolean = false
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
