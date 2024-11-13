package com.jydev.worksheet.presentation.worksheet.model.response

import com.jydev.worksheet.domain.problem.ProblemType
import io.swagger.v3.oas.annotations.media.Schema

data class GetWorksheetProblemsResponse(
    @Schema(
        description = """
        문제 리스트
        """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val items : List<GetWorksheetProblemsItemResponse>
) {
    data class GetWorksheetProblemsItemResponse(

        @Schema(
            description = """
            문제 id
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val problemId : Long,

        @Schema(
            description = """
            문제 유형 코드
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val unitCode: String,

        @Schema(
            description = """
            문제 난이도
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val level: Int,

        @Schema(
            description = """
            문제 종류
            SUBJECTIVE[주관식]
            SELECTION[객관식]
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val problemType: ProblemType,
    )
}
