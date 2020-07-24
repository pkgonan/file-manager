package io.filemanager.s3.infra

import io.filemanager.s3.domain.S3Repository
import io.filemanager.s3.domain.UploadPart
import io.filemanager.s3.domain.UploadPartResponse
import io.filemanager.s3.exception.S3MultipartUploadFailureException
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.s3.S3AsyncClient

@Repository
class S3RepositoryImpl(
    client: S3AsyncClient
): S3Repository {

    private val s3MultipartUploadUtil =
        S3MultipartUploadUtil(client)

    override fun upload(part: UploadPart): Mono<UploadPartResponse> {
        val createRequest = s3MultipartUploadUtil.getCreateRequest(part)
        val create = s3MultipartUploadUtil.create(createRequest)

        // TODO : Implementation
        return Mono.empty()
    }
}