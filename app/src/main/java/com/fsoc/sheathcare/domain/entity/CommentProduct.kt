package com.fsoc.sheathcare.domain.entity

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

data class CommentProduct(
    var avatar: String = "",
    var seader: String = "",
    var content: String = "",
    var timeStamp : FieldValue ?= null
)