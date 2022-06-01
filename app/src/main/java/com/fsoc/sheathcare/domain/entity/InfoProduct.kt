package com.fsoc.sheathcare.domain.entity

data class InfoProduct(
    val idComment: String ="",
    val image: String = "",
    val ingredient: String = "",
    val licenseNumber: String = "",
    val likeNumber: Int = 0,
    val name: String = "",
    val origin: String = "",
    val sideEfects: String = "",
    val type: String = "",
    val userManual: String = "",
    val uses: String = ""
)