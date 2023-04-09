package com.programasoft.application.certificate

interface CertificateSummary {
    val id: Long
    val title: String
    val description: String
    val image: ByteArray
}