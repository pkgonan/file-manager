package io.filemanager.s3.domain

class CompletedPart(
    private val eTag: String,
    private val number: Int
) {
    fun getETag() = eTag
    fun getNumber() = number
}