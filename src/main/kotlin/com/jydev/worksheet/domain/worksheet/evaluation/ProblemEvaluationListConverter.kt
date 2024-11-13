package com.jydev.worksheet.domain.worksheet.evaluation

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class ProblemEvaluationListConverter : AttributeConverter<List<ProblemEvaluation>, String> {

    override fun convertToDatabaseColumn(attribute: List<ProblemEvaluation>?): String {
        return attribute?.sortedBy { it.problemIndex }
            ?.joinToString(",") { if (it.correct) "1" else "0" }
            ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<ProblemEvaluation> {
        return dbData?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.mapIndexed { index, correct -> ProblemEvaluation(index, correct == "1") }
            ?: emptyList()
    }
}