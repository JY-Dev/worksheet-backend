package com.jydev.worksheet.application.worksheet.model

data class WorksheetAnalysisResultModel(
    val worksheetId: Long,
    val worksheetName : String,
    val completedEvaluationCount : Int,
    val studentTrainingResults : List<StudentTrainingResult>,
    val problemAnalysisResults: List<ProblemAnalysisResult>
) {
    data class StudentTrainingResult(
        val studentId: Long,
        val studentName : String,
        val evaluated : Boolean,
        val correctWorksheetRate : Int?,
    )

    data class ProblemAnalysisResult(
        val problemId : Long,
        val correctPerProblemRate : Int?,
    )
}
