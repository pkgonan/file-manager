package io.filemanager.s3.domain

import reactor.core.publisher.Mono

interface S3Repository {

    fun upload(part: UploadPart): Mono<UploadPartResponse>

}