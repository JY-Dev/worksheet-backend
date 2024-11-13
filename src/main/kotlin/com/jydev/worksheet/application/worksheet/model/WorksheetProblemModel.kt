package com.jydev.worksheet.application.worksheet.model

import com.jydev.worksheet.domain.problem.ProblemType
import com.jydev.worksheet.domain.problem.UnitCode

data class WorksheetProblemModel(
    val problemId : Long,
    val unitCode : UnitCode,
    val difficulty : Int,
    val problemType : ProblemType,
)
