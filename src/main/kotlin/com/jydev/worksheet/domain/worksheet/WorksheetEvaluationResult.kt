package com.jydev.worksheet.domain.worksheet

import com.jydev.worksheet.domain.worksheet.evaluation.EvaluatedAnswer
import com.jydev.worksheet.core.jpa.converter.BooleanListConverter
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable

@Embeddable
class WorksheetEvaluationResult private constructor(
    evaluatedAnswers: List<EvaluatedAnswer>
) {

    @Convert(converter = BooleanListConverter::class)
    final val problemEvaluations: List<Boolean> = evaluatedAnswers.map { answer ->
        answer.correct
    }

    final val correctCount = problemEvaluations.count { it }

    fun hasNoResult() = problemEvaluations.isEmpty()

    companion object {
        fun createNoResult() = WorksheetEvaluationResult(emptyList())
        fun createResult(
            evaluatedAnswers: List<EvaluatedAnswer>
        ) = WorksheetEvaluationResult(evaluatedAnswers)
    }
}