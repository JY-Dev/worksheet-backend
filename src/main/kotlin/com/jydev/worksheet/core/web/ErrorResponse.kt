package com.mindshare.api.core.web

import com.jydev.worksheet.core.error.ErrorCode

data class ErrorResponse(
    val message: String,
    val errorCode: ErrorCode
)
