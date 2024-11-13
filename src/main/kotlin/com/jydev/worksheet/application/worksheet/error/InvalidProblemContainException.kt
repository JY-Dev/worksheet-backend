package com.jydev.worksheet.application.worksheet.error

import com.jydev.worksheet.core.error.ErrorCode
import com.mindshare.api.core.error.BusinessException

class InvalidProblemContainException(message: String) : BusinessException(message, ErrorCode.A01002) {
}