package com.programasoft.application.attachment

interface AttachmentSummary {
    var id: Long
    val name: String
    val type: String
    val content: ByteArray
}