package com.jydev.worksheet.application.worksheet

interface WorksheetFinder {
    fun searchAssignedWorksheetStudentIds(worksheetId : Long) : List<Long>
}