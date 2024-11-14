package com.jydev.worksheet.core.jpa.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class BooleanListConverter : AttributeConverter<List<Boolean>, String> {

    override fun convertToDatabaseColumn(attribute: List<Boolean>?): String {
        return attribute?.joinToString(",") { if (it) "1" else "0" }
            ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<Boolean> {
        return dbData?.split(",")
            ?.filter { it.isNotEmpty() }
            ?.map{correct -> correct == "1"}
            ?: emptyList()
    }
}