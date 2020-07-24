package io.filemanager.s3.domain

class UploadPartResponse(
    private val fileName: String,
    private val bucketName: String
) {
    fun getFileName() = fileName
    fun getBucketName() = bucketName
}