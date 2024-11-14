package com.jydev.worksheet.core.web

import com.mindshare.api.core.error.BusinessAuthorizationException
import com.mindshare.api.core.error.BusinessException
import com.jydev.worksheet.core.error.ErrorCode
import com.mindshare.api.core.log.logger
import com.mindshare.api.core.web.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.util.WebUtils

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    private val log = logger()

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException::class)
    fun handleError(e: BusinessException): ErrorResponse {
        log.debug(e.message)
        return ErrorResponse(
            e.message ?: "Unknown error",
            e.errorCode,
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleError(e: Exception): ErrorResponse {
        log.debug(e.message)
        return ErrorResponse(
            e.message ?: "Unknown error",
            ErrorCode.DEFAULT,
        )
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException::class)
    fun handleError(e: NoSuchElementException): ErrorResponse {
        log.debug(e.message)
        return ErrorResponse(
            e.message ?: "Unknown error",
            ErrorCode.DEFAULT,
        )
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun handleServerError(e: Exception): ErrorResponse {
        log.error(e.message)
        return ErrorResponse(
            e.message ?: "Unknown error",
            ErrorCode.DEFAULT,
        )
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(BusinessAuthorizationException::class)
    fun handleError(e: BusinessAuthorizationException): ErrorResponse {
        log.debug(e.message)
        return ErrorResponse(
            e.message ?: "Unknown error",
            e.errorCode
        )
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {

        val errorResponse = ErrorResponse(
            ex.message,
            ErrorCode.A05001,
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    override fun createResponseEntity(
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {

        val exception =
            request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST) as? Exception
        val errorResponse = ErrorResponse(exception?.message ?: "Unknown error", ErrorCode.DEFAULT)

        return ResponseEntity(errorResponse, statusCode)
    }
}