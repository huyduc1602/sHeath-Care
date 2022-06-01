package com.fsoc.sheathcare.domain.entity

data class User(
    var email: String? = "",
    var name: String? = "",
    var avatar: String? = "",
    var age: Int? = 0,
    var type: Int? = 0,
    var token: String? = ""
)