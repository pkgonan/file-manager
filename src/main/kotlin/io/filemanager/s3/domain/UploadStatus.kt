package io.filemanager.s3.domain

import java.util.concurrent.atomic.AtomicInteger

class UploadStatus(
    private val bucketName: String,
    private val fileKey: String,
    private val uploadId: String
) {
    private val partCounter: AtomicInteger = AtomicInteger()
    private val completedParts: Map<Int, CompletedPart> = emptyMap()

    fun increaseAndGetPartCounter() = partCounter.incrementAndGet()
    fun getBucketName() = bucketName
    fun getFileKey() = fileKey
    fun getUploadId() = uploadId
    fun getCompletedParts() = completedParts
}