package com.jydev.worksheet.domain.worksheet.evaluation

data class EvaluatedAnswer(
    val problemId : Long,
    val correct: Boolean,
    val submittedAnswer : String,
    val expectedAnswer : String,
)
