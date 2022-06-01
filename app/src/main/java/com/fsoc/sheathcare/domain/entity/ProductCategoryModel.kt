package com.fsoc.sheathcare.domain.entity

data class ProductCategoryModel(
    val categoryLists: List<String> = listOf(""),
    val name: String? = "",
    val key: String = ""
)