package io.filemanager.s3.service

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class S3UploadService {

    // TODO : Implementation
    fun upload(): Flux<String> {
        return Flux.empty()
    }
}