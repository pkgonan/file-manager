package io.filemanager.s3.infra

import io.filemanager.s3.domain.CompletedPart
import io.filemanager.s3.domain.UploadPart
import io.filemanager.s3.domain.UploadStatus
import reactor.core.publisher.Mono
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadResponse
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse
import software.amazon.awssdk.services.s3.model.UploadPartRequest
import software.amazon.awssdk.services.s3.model.UploadPartResponse
import java.nio.ByteBuffer

class S3MultipartUploadUtil(
    private val client: S3AsyncClient
) {

    internal fun create(request: CreateMultipartUploadRequest): Mono<CreateMultipartUploadResponse> {
        return Mono.fromFuture(client.createMultipartUpload(request))
    }

    internal fun upload(request: UploadPartRequest, buffer: ByteBuffer): Mono<UploadPartResponse> {
        return Mono.fromFuture(client.uploadPart(request, AsyncRequestBody.fromPublisher(Mono.just(buffer))))
    }

    internal fun complete(request: CompleteMultipartUploadRequest): Mono<CompleteMultipartUploadResponse> {
        return Mono.fromFuture(client.completeMultipartUpload(request))
    }

    internal fun abort(request: AbortMultipartUploadRequest): Mono<AbortMultipartUploadResponse> {
        return Mono.fromFuture(client.abortMultipartUpload(request))
    }

    internal fun getCreateRequest(part: UploadPart): CreateMultipartUploadRequest {
        return CreateMultipartUploadRequest.builder()
            .bucket(part.getBucketName())
            .key(part.getFileKey())
            .contentType(part.getContentType())
            .metadata(emptyMap())
            .build()
    }

    internal fun getUploadRequest(status: UploadStatus, buffer: ByteBuffer): UploadPartRequest {
        return UploadPartRequest.builder()
            .bucket(status.getBucketName())
            .key(status.getFileKey())
            .partNumber(status.increaseAndGetPartCounter())
            .uploadId(status.getUploadId())
            .contentLength(buffer.capacity().toLong())
            .build()
    }

    internal fun getCompleteRequest(status: UploadStatus): CompleteMultipartUploadRequest {
        return CompleteMultipartUploadRequest.builder()
            .bucket(status.getBucketName())
            .key(status.getFileKey())
            .uploadId(status.getUploadId())
            .multipartUpload(getCompleteUpload(status))
            .build()
    }

    internal fun getAbortRequest(status: UploadStatus): AbortMultipartUploadRequest {
        return AbortMultipartUploadRequest.builder()
            .bucket(status.getBucketName())
            .key(status.getFileKey())
            .uploadId(status.getUploadId())
            .build()
    }

    private fun getCompleteUpload(status: UploadStatus): CompletedMultipartUpload {
        return CompletedMultipartUpload.builder()
            .parts(status.getCompletedParts().values.map { toCompletedPart(it) })
            .build()
    }

    private fun toCompletedPart(part: CompletedPart): software.amazon.awssdk.services.s3.model.CompletedPart {
        return software.amazon.awssdk.services.s3.model.CompletedPart.builder()
            .eTag(part.getETag())
            .partNumber(part.getNumber())
            .build()
    }
}