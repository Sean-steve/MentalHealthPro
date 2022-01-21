package com.sean_steve.mentalhealthpro.blog

class ModelComments {
    var cId: String? = null
    var comment: String? = null
    var ptime: String? = null
    var udp: String? = null
    fun getcId(): String? {
        return cId
    }

    fun setcId(cId: String?) {
        this.cId = cId
    }

    var uemail: String? = null

    constructor() {}

    var uid: String? = null

    constructor(
        cId: String?,
        comment: String?,
        ptime: String?,
        udp: String?,
        uemail: String?,
        uid: String?,
        uname: String?
    ) {
        this.cId = cId
        this.comment = comment
        this.ptime = ptime
        this.udp = udp
        this.uemail = uemail
        this.uid = uid
        this.uname = uname
    }

    var uname: String? = null
}