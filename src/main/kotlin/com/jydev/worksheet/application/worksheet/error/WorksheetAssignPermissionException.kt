package com.jydev.worksheet.application.worksheet.error

import com.jydev.worksheet.core.error.ErrorCode
import com.mindshare.api.core.error.BusinessAuthorizationException

class WorksheetAssignPermissionException(message: String) : BusinessAuthorizationException(message, ErrorCode.DEFAULT)