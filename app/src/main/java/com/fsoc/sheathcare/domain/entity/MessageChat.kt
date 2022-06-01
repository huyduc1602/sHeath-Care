package com.fsoc.sheathcare.domain.entity

import java.util.*

object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}

interface MessageChat {
    val time: Date
    val senderId: String
    val recipientId: String
    val senderName: String
    val type: String
}