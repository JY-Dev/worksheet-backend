package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.worksheet.model.StudentModel
import com.jydev.worksheet.application.worksheet.model.WorksheetAnalysisModel

interface WorksheetFinder {
    fun assignedStudents(worksheetId : Long) : List<StudentModel>
    fun searchAssignedWorksheetStudentIds(worksheetId : Long) : List<Long>
    fun searchWorksheetAnalysisDataList(worksheetId: Long) : List<WorksheetAnalysisModel>
}