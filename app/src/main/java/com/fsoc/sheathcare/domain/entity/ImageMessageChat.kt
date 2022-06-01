package com.fsoc.sheathcare.domain.entity

import java.util.*

data class ImageMessageChat(val imagePath: String,
                            override val time: Date,
                            override val senderId: String,
                            override val recipientId: String,
                            override val senderName: String,
                            override val type: String = MessageType.IMAGE)
    : MessageChat {
    constructor() : this("", Date(0), "", "", "")
}