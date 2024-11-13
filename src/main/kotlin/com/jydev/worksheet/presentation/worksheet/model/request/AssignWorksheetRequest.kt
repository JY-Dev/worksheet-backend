package com.jydev.worksheet.presentation.worksheet.model.request

import io.swagger.v3.oas.annotations.media.Schema

data class AssignWorksheetRequest(

    @Schema(
        description = """
        학습지 id
        """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val worksheetId: Long,

    @Schema(
        description = """
        선생님 id
        """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val teacherId: Long
)