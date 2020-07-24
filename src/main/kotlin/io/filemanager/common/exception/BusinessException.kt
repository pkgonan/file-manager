package io.filemanager.common.exception

open class BusinessException(
    val code: String,
    override val message: String? = null
) : RuntimeException(message)