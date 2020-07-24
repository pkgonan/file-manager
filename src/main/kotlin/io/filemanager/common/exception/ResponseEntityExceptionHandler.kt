package io.filemanager.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ResponseEntityExceptionHandler {

    companion object {
        val log = LoggerFactory.getLogger(ResponseEntityExceptionHandler::class.java)!!
    }

    @ExceptionHandler(NotFoundException::class)
    protected fun handleNotFoundException(e: NotFoundException): ResponseEntity<Response> {
        log.debug("handleNotFoundException ({})", e)
        val response =
            Response(
                null,
                e.message
            )
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<Response> {
        log.debug("handleBusinessException ({})", e)
        val response =
            Response(
                e.code,
                e.message
            )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    protected fun handleUncaughtException(e: Exception): ResponseEntity<Response> {
        log.error("handleUncaughtException ({})", e)
        val response =
            Response(
                null,
                "Internal Server Error"
            )
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    data class Response(
        val code: String?,
        val message: String?,
        val timestamp: LocalDateTime = LocalDateTime.now()
    )
}