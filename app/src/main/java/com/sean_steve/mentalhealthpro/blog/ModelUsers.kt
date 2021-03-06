package com.sean_steve.mentalhealthpro.blog

class ModelUsers {
    var name: String? = null

    constructor() {}

    var onlineStatus: String? = null
    var typingTo: String? = null

    constructor(
        name: String?,
        onlineStatus: String?,
        typingTo: String?,
        email: String?,
        image: String?,
        uid: String?
    ) {
        this.name = name
        this.onlineStatus = onlineStatus
        this.typingTo = typingTo
        this.email = email
        this.image = image
        this.uid = uid
    }

    var email: String? = null
    var image: String? = null
    var uid: String? = null
}