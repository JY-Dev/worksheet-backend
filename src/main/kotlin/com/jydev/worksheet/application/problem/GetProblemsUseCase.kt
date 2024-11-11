package com.jydev.worksheet.application.problem

import com.jydev.worksheet.application.problem.model.ProblemModel
import com.jydev.worksheet.application.problem.model.SearchCriteria
import com.jydev.worksheet.domain.problem.ProblemLevel
import org.springframework.stereotype.Service

@Service
class GetProblemsUseCase(
    private val finder: ProblemFinder
) {

    operator fun invoke(searchCriteria: SearchCriteria): List<ProblemModel> {
        val problemCountCalculator = ProblemCountCalculator.fromProblemLevel(searchCriteria.level)

        val problems = ProblemLevel.entries
            .flatMap { level ->
                val levelSpecificSize = problemCountCalculator.calculateProblemCount(
                    searchCriteria.totalSize,
                    level
                )

                val adjustedCriteria = searchCriteria.copy(
                    totalSize = levelSpecificSize,
                    level = level
                )

                finder.searchProblems(adjustedCriteria)
            }

        return problems.take(searchCriteria.totalSize)
    }
}