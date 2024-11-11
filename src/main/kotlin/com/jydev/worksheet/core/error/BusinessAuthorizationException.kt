package com.mindshare.api.core.error

import com.jydev.worksheet.core.error.ErrorCode

open class BusinessAuthorizationException(
    message: String,
    val errorCode: ErrorCode
) : RuntimeException(message) {
}