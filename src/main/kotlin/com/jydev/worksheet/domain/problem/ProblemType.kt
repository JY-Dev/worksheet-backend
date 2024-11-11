package com.jydev.worksheet.domain.problem

enum class ProblemType(
    private val description: String
) {
    SUBJECTIVE("주관식"),
    SELECTION("객관식");
}