package com.jydev.worksheet.core.jpa.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.jydev.worksheet.domain.worksheet.evaluation.SubmittedAnswer
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.stereotype.Component

@Component
@Converter
class SubmittedAnswerListConverter(
    private val objectMapper: ObjectMapper
) : AttributeConverter<List<SubmittedAnswer>, String> {

    override fun convertToDatabaseColumn(attribute: List<SubmittedAnswer>?): String {
        return attribute?.let {
            try {
                objectMapper.writeValueAsString(it)
            } catch (e: Exception) {
                throw IllegalArgumentException("Error converting list of SubmittedAnswer to JSON", e)
            }
        } ?: "[]"
    }

    override fun convertToEntityAttribute(dbData: String?): List<SubmittedAnswer> {
        return dbData?.let {
            try {
                objectMapper.readValue(it)
            } catch (e: Exception) {
                throw IllegalArgumentException("Error converting JSON to list of SubmittedAnswer", e)
            }
        } ?: emptyList()
    }
}
