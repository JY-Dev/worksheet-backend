package com.jydev.worksheet.presentation.worksheet.model.response

import io.swagger.v3.oas.annotations.media.Schema

data class AnalyzeWorksheetResponse(

    @Schema(
        description = """
            학습지 ID
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val worksheetId : Long,

    @Schema(
        description = """
            학습지 이름
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val worksheetName: String,

    @Schema(
        description = """
            학습지 채점 완료된 학생 수
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val completedEvaluationCount : Int,

    @Schema(
        description = """
            Assign된 학생들의 학습 결과 목록
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val studentTrainingResults : List<StudentTrainingResult>,

    @Schema(
        description = """
            학습지 문제 분석 결과 목록
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val problemAnalysisResults: List<ProblemAnalysisResult>,
) {
    data class StudentTrainingResult(
        @Schema(
            description = """
            학생 ID
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val studentId : Long,

        @Schema(
            description = """
            학생 이름
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val studentName : String,

        @Schema(
            description = """
            채점 여부
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val evaluated : Boolean,

        @Schema(
            description = """
            학습지 정답률 (학습지 채점이 완료되지 않았다면 null)
            """,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
        )
        val correctWorksheetRate : Int?,
    )

    data class ProblemAnalysisResult(

        @Schema(
            description = """
            문제 ID
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val problemId : Long,

        @Schema(
            description = """
            문제 정답률 (학습지 채점 완료된 학생 수가 0 인경우 null)
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        val correctPerProblemRate : Int?
    )
}
