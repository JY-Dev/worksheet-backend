package com.jydev.worksheet.infra.problem

import com.jydev.worksheet.application.problem.ProblemFinder
import com.jydev.worksheet.application.problem.model.ProblemModel
import com.jydev.worksheet.application.problem.model.SearchCriteria
import com.jydev.worksheet.application.problem.model.SearchProblemType
import com.jydev.worksheet.domain.problem.QProblem
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QueryDslProblemFinder(
    private val queryFactory : JPAQueryFactory
) : ProblemFinder {

    override fun searchProblems(searchCriteria: SearchCriteria): List<ProblemModel> {
        val qProblem = QProblem.problem
        return queryFactory.select(problemProjection(problem = qProblem))
            .from(qProblem)
            .condition(problem = qProblem, searchCriteria = searchCriteria)
            .orderBy(qProblem.id.desc())
            .fetch()
    }

    private fun problemProjection(problem: QProblem): Expression<ProblemModel> {
        return Projections.constructor(
            ProblemModel::class.java,
            problem.id,
            problem.answer,
            problem.unitCode,
            problem.difficulty,
            problem.type
        )
    }

    // index : IDX_PROBLEM_SEARCH
    fun JPAQuery<ProblemModel>.condition(problem: QProblem, searchCriteria: SearchCriteria) : JPAQuery<ProblemModel> {

        val unitCodes = searchCriteria.unitCodes
        if(unitCodes.isNotEmpty()) {
            val matchUnitCodes = problem.unitCode.`in`(unitCodes)
            this.where(matchUnitCodes)
        }

        val searchProblemType = searchCriteria.searchProblemType
        if(searchProblemType != SearchProblemType.ALL) {
            val problemType = searchProblemType.toProblemType()
            val matchProblemType = problem.type.eq(problemType)
            this.where(matchProblemType)
        }

        val level = searchCriteria.level
        val difficultyList = level.difficultyRange.toList()
        val matchDifficulties = problem.difficulty.`in`(difficultyList)
        this.where(matchDifficulties)

        return this
    }
}