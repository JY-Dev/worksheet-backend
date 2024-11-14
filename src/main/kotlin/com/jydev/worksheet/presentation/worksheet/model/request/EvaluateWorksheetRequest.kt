package com.jydev.worksheet.presentation.worksheet.model.request

import io.swagger.v3.oas.annotations.media.Schema

data class EvaluateWorksheetRequest(

    @Schema(
        description = """
        학생 id
        """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val studentId: Long,

    @Schema(
        description = """
        제출 답안 목록
        """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val submittedAnswers: List<AnswerRequest>
) {
    data class AnswerRequest(

        @Schema(
            description = """
            문제 ID
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val problemId: Long,

        @Schema(
            description = """
            제출 답안
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val submittedAnswer: String
    )
}
