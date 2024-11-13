package com.jydev.worksheet.domain.worksheet

interface AssignedWorksheetRepository {
    fun findById(id: Long): AssignedWorksheet?
    fun findByWorksheetIdAndLearnerId(worksheetId: Long, leanerId : Long): AssignedWorksheet?
    fun save(worksheet: AssignedWorksheet): AssignedWorksheet
    fun delete(worksheet: AssignedWorksheet)
}