package com.jydev.worksheet.application.problem.model

import com.jydev.worksheet.domain.problem.ProblemType
import com.jydev.worksheet.domain.problem.UnitCode

data class ProblemModel(
    val problemId : Long,
    val answer : String,
    val unitCode : UnitCode,
    val difficulty : Int,
    val problemType : ProblemType,
)
