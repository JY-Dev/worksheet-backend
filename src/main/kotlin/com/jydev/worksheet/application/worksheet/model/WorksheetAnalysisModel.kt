package com.jydev.worksheet.application.worksheet.model

data class WorksheetAnalysisModel(
    val studentId : Long,
    val problemEvaluations : List<Boolean>,
    val correctCount : Int,
)
