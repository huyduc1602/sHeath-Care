package com.fsoc.sheathcare.domain.entity

class Notification {
    var title: String
    var body : String

    constructor(title: String, body: String) {
        this.title = title
        this.body = body
    }
}