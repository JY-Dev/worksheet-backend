package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.problem.ProblemFinder
import com.jydev.worksheet.application.worksheet.model.WorksheetProblemModel
import com.jydev.worksheet.domain.worksheet.WorksheetRepository
import org.springframework.stereotype.Service

@Service
class GetWorksheetProblemsUseCase(
    private val worksheetRepository: WorksheetRepository,
    private val problemFinder: ProblemFinder
) {
    operator fun invoke(worksheetId: Long): List<WorksheetProblemModel> {
        val worksheet = worksheetRepository.findById(worksheetId)
            ?: throw NoSuchElementException("Worksheet with id $worksheetId does not exist")
        val problemIdOrderMap = worksheet.problemIds
            .withIndex()
            .associate {
                it.value to it.index
            }

        val problems = problemFinder.searchProblems(problemIds = worksheet.problemIds)
        return problems.sortedBy { problemIdOrderMap[it.problemId] }
            .map { problem ->
                WorksheetProblemModel(
                    problemId = problem.problemId,
                    unitCode = problem.unitCode,
                    difficulty = problem.difficulty,
                    problemType = problem.problemType
                )
            }
    }
}