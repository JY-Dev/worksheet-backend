package com.jydev.worksheet.application.problem

import com.jydev.worksheet.application.problem.model.ProblemModel
import com.jydev.worksheet.application.problem.model.SearchCriteria

interface ProblemFinder {
    fun searchProblems(searchCriteria: SearchCriteria) : List<ProblemModel>
    fun searchProblems(problemIds : List<Long>) : List<ProblemModel>
    fun countExistingProblems(ids: List<Long>) : Int
}