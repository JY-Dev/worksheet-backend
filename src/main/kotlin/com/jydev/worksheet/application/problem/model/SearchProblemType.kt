package com.jydev.worksheet.application.problem.model

import com.jydev.worksheet.domain.problem.ProblemType

enum class SearchProblemType(
    private val description: String
) {
    ALL("전체 조회"),
    SUBJECTIVE("주관식"),
    SELECTION("객관식");

    fun toProblemType() : ProblemType? {
        return when(this){
            SUBJECTIVE -> ProblemType.SUBJECTIVE
            SELECTION -> ProblemType.SELECTION
            ALL -> null
        }
    }
}