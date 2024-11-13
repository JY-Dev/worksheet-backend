package com.jydev.worksheet.domain.worksheet

import com.jydev.worksheet.domain.worksheet.evaluation.EvaluatedAnswer
import com.jydev.worksheet.domain.worksheet.evaluation.ProblemEvaluation
import com.jydev.worksheet.domain.worksheet.evaluation.ProblemEvaluationListConverter
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable

@Embeddable
class WorksheetEvaluationResult private constructor(
    problemOrderMap: Map<Long, Int>,
    evaluatedAnswers: List<EvaluatedAnswer>
) {

    @Convert(converter = ProblemEvaluationListConverter::class)
    final val problemEvaluations: List<ProblemEvaluation> = evaluatedAnswers.map { answer ->
        val problemId = answer.problemId
        val idx = problemOrderMap[problemId]
            ?: throw IllegalArgumentException("Problem ID $problemId not found in the map.")
        ProblemEvaluation(idx, answer.correct)
    }

    final val correctCount = problemEvaluations.count { it.correct }

    fun hasNoResult() = problemEvaluations.isEmpty()

    companion object {
        fun createNoResult() = WorksheetEvaluationResult(emptyMap(), emptyList())
        fun createResult(
            problemOrderMap: Map<Long, Int>,
            evaluatedAnswers: List<EvaluatedAnswer>
        ) = WorksheetEvaluationResult(problemOrderMap, evaluatedAnswers)
    }
}