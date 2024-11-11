package com.jydev.worksheet.core.jpa.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime


@Converter
class InstantToUtcConverter : AttributeConverter<Instant?, LocalDateTime?> {

    override fun convertToDatabaseColumn(instant: Instant?): LocalDateTime? =
        instant?.let{
            LocalDateTime.ofInstant(instant, Clock.systemUTC().zone)
        }

    override fun convertToEntityAttribute(localDateTime: LocalDateTime?): Instant? =
        localDateTime?.atZone(Clock.systemUTC().zone)?.toInstant()
}