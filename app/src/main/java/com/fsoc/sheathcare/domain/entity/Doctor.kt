package com.fsoc.sheathcare.domain.entity

data class Doctor(
    val name: String ?= null,
    val avatar: String ?=null,
    val specialize : String ?=null
)