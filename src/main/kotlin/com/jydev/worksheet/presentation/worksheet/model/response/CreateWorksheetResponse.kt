package com.jydev.worksheet.presentation.worksheet.model.response

import io.swagger.v3.oas.annotations.media.Schema

data class CreateWorksheetResponse(

    @Schema(
        description = """
            학습지 id
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val worksheetId : Long,

    @Schema(
        description = """
            선생님 id
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val teacherId : Long,

    @Schema(
        description = """
            학습지 이름
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val worksheetName : String,

    @Schema(
        description = """
            문제 id 리스트 (학습지 문제 번호 순서대로)
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val problemIds : List<Long>,
)
