package com.fsoc.sheathcare.domain.entity

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}