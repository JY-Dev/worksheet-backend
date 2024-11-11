package com.jydev.worksheet.application.problem.model

import com.jydev.worksheet.domain.problem.ProblemLevel
import com.jydev.worksheet.domain.problem.UnitCode

data class SearchCriteria(
    val unitCodes: List<UnitCode>,
    val searchProblemType: SearchProblemType,
    val level: ProblemLevel,
    val totalSize: Int
)
