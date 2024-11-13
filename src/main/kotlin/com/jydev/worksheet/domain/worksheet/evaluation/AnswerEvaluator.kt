package com.jydev.worksheet.domain.worksheet.evaluation

interface AnswerEvaluator {
    fun evaluate(answers : List<SubmittedAnswer>) : List<EvaluatedAnswer>
}