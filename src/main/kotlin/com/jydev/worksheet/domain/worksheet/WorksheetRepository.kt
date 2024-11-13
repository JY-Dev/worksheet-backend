package com.jydev.worksheet.domain.worksheet

interface WorksheetRepository {
    fun findById(id: Long): Worksheet?
    fun existsByCreatorIdAndName(creatorId: Long, name: String): Boolean
    fun findByCreatorId(creatorId: Long): List<Worksheet>
    fun save(worksheet: Worksheet): Worksheet
    fun delete(worksheet: Worksheet)
}