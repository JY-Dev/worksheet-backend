package com.jydev.worksheet.presentation.worksheet.model.response

import io.swagger.v3.oas.annotations.media.Schema

data class EvaluateWorksheetResponse(
    @Schema(
        description = """
        체점 결과 리스트 (문제 순서대로)
        """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val items : List<EvaluateWorksheetItemResponse>
) {
    data class EvaluateWorksheetItemResponse(

        @Schema(
            description = """
            문제 id
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val problemId : Long,

        @Schema(
            description = """
            정답 여부
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val collect : Boolean,
    )
}
