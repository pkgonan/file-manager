package io.filemanager.s3

import io.filemanager.s3.service.S3UploadService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class S3UploadController(
    private val uploadService: S3UploadService
) {

    // TODO : produces = MediaType.APPLICATION_STREAM_JSON_VALUE 유무 차이 검사
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
    fun upload(@RequestHeader headers: HttpHeaders, @RequestBody parts: Flux<FilePart>): Flux<String> {

        // TODO : Implementation
        return uploadService.upload()
    }
}