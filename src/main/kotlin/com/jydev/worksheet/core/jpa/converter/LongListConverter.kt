package com.jydev.worksheet.core.jpa.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class LongListConverter : AttributeConverter<List<Long>, String> {
    override fun convertToDatabaseColumn(attribute: List<Long>?): String {
        return attribute?.joinToString(",") ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<Long> {
        return dbData?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.map { it.toLong() }
            ?: emptyList()
    }
}