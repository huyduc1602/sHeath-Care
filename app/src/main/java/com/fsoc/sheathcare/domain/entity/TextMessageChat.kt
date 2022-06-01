package com.fsoc.sheathcare.domain.entity

import java.util.*

data class TextMessageChat(
    val text: String,
    override val time: Date,
    override val senderId: String,
    override val recipientId: String,
    override val senderName: String,
    override val type: String = MessageType.TEXT
) : MessageChat {
    constructor() : this("", Date(0), "", "", "")
}