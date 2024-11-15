package com.jydev.worksheet.presentation.problem

import com.jydev.worksheet.application.problem.GetProblemsUseCase
import com.jydev.worksheet.application.problem.model.SearchCriteria
import com.jydev.worksheet.application.problem.model.SearchProblemType
import com.jydev.worksheet.domain.problem.ProblemLevel
import com.jydev.worksheet.domain.problem.UnitCode
import com.jydev.worksheet.presentation.problem.model.response.GetProblemsResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class ProblemApiController(
    private val getProblemsUseCase: GetProblemsUseCase,
) : ProblemApi {
    override fun getProblems(
        totalCount: Int,
        level: ProblemLevel,
        problemType: SearchProblemType,
        unitCodeList: List<String>
    ): GetProblemsResponse {

        val unitCodes = unitCodeList.map { code -> UnitCode(code)}
        val searchCriteria = SearchCriteria(
            unitCodes = unitCodes,
            searchProblemType = problemType,
            level = level,
            totalSize = totalCount
        )

        val items = getProblemsUseCase(searchCriteria).map { problem ->
            GetProblemsResponse.GetProblemsItemResponse(
                id = problem.id,
                unitCode = problem.unitCode.value(),
                level = problem.difficulty,
                problemType = problem.problemType,
                answer = problem.answer
            )
        }.toList()

        return GetProblemsResponse(items)
    }
}