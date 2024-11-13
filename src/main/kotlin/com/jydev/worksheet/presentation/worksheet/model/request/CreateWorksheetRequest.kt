package com.jydev.worksheet.presentation.worksheet.model.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "Create Worksheet Request")
data class CreateWorksheetRequest(

    @Schema(
        description = """
        선생님 id
        """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val teacherId: Long,

    @field:Size(min = 1, max = 50)
    @Schema(
        description = """
        문제 id 리스트 (학습지 문제 번호 순서대로)
        """,
        requiredMode = Schema.RequiredMode.REQUIRED,
    )
    val problemIds: List<Long>,

    @field:Size(min = 1, max = 20)
    @Schema(
        description = """
        학습지 이름
        """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val worksheetName: String
)
