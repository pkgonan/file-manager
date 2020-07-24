package io.filemanager.s3.exception

import io.filemanager.common.exception.BusinessException

class S3MultipartUploadFailureException: BusinessException(
    ERROR_CODE, "S3 multipart upload failure") {
    companion object {
        private const val ERROR_CODE = "s3.multipart.upload.failure"
    }
}