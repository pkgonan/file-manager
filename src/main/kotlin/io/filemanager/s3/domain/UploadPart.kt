package io.filemanager.s3.domain

class UploadPart(
    private val number: Int,
    private val data: ByteArray,
    private val fileKey: String,
    private val bucketName: String,
    private val contentType: String
) {
    fun getNumber() = number
    fun getData() = data
    fun getFileKey() = fileKey
    fun getBucketName() = bucketName
    fun getContentType() = contentType
}