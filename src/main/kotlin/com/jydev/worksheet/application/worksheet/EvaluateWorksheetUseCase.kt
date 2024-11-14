package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.worksheet.error.WorksheetAlreadyEvaluatedException
import com.jydev.worksheet.domain.worksheet.AssignedWorksheetRepository
import com.jydev.worksheet.domain.worksheet.evaluation.AnswerEvaluator
import com.jydev.worksheet.domain.worksheet.evaluation.SubmittedAnswer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EvaluateWorksheetUseCase(
    private val assignedWorksheetRepository: AssignedWorksheetRepository,
    private val answerEvaluator: AnswerEvaluator
) {

    @Transactional
    operator fun invoke(studentId: Long, worksheetId: Long, answers: List<SubmittedAnswer>): List<Pair<Long, Boolean>> {
        val assignedWorksheet = assignedWorksheetRepository.findByWorksheetIdAndLearnerId(worksheetId, studentId)
            ?: throw NoSuchElementException("AssignedWorksheet with id $worksheetId does not exist")

        if (assignedWorksheet.isAlreadyEvaluated()) {
            throw WorksheetAlreadyEvaluatedException("Worksheet with id $worksheetId already evaluated")
        }

        assignedWorksheet.evaluate(answerEvaluator, answers)
        val evaluationResult = assignedWorksheet.evaluationResult
        val problemEvaluations = evaluationResult.problemEvaluations

        return problemEvaluations.mapIndexed { idx, correct ->
            val problemId = answers[idx].problemId
            problemId to correct
        }
    }
}