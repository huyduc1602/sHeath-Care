package com.fsoc.sheathcare.data.api.entity

data class BaseDto(
    val message: String?,
    val status: Int?,
    val errors: List<String>?
)