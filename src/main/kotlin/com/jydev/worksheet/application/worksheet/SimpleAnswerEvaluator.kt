package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.problem.ProblemFinder
import com.jydev.worksheet.core.util.toIndexMap
import com.jydev.worksheet.domain.worksheet.evaluation.AnswerEvaluator
import com.jydev.worksheet.domain.worksheet.evaluation.EvaluatedAnswer
import com.jydev.worksheet.domain.worksheet.evaluation.SubmittedAnswer
import org.springframework.stereotype.Service

@Service
class SimpleAnswerEvaluator(
    private val problemFinder: ProblemFinder
) : AnswerEvaluator {
    override fun evaluate(answers: List<SubmittedAnswer>): List<EvaluatedAnswer> {
        val evaluationProblemIds = answers.map { it.problemId }
        val evaluationProblemIdIndexMap = evaluationProblemIds.toIndexMap()
        val evaluationProblems = problemFinder.searchProblems(evaluationProblemIds)
            .sortedBy { evaluationProblemIdIndexMap[it.problemId] }

        return evaluationProblems.mapIndexed { idx, problem ->
            val submitAnswer = answers[idx]
            val correctAnswer = submitAnswer.answer == problem.answer

            EvaluatedAnswer(
                problemId = problem.problemId,
                correct = correctAnswer,
                submittedAnswer = submitAnswer.answer,
                expectedAnswer = problem.answer
            )
        }
    }
}