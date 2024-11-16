package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.worksheet.model.StudentModel
import com.jydev.worksheet.application.worksheet.model.WorksheetAnalysisModel
import com.jydev.worksheet.domain.worksheet.Worksheet
import com.jydev.worksheet.domain.worksheet.WorksheetRepository
import com.jydev.worksheet.domain.worksheet.user.Teacher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest

class AnalyzeWorksheetUseCaseTest {

    private val worksheetRepository = mock(WorksheetRepository::class.java)
    private val worksheetFinder = mock(WorksheetFinder::class.java)
    private val analyzeWorksheetUseCase = AnalyzeWorksheetUseCase(worksheetRepository, worksheetFinder)


    @Test
    fun `올바른 학습지 분석 결과를 반환한다`() {
        // Given
        val worksheetId = 1L
        val teacher = Teacher("Teacher", 1)
        val worksheet = Worksheet("test", listOf(101L, 102L), teacher, worksheetId)
        `when`(worksheetRepository.findById(worksheetId)).thenReturn(worksheet)

        val analysisDataList = listOf(
            WorksheetAnalysisModel(studentId = 1L, problemEvaluations = listOf(true,false), correctCount = 1),
            WorksheetAnalysisModel(studentId = 2L, problemEvaluations = listOf(false,false), correctCount = 0)
        )
        `when`(worksheetFinder.searchWorksheetAnalysisDataList(worksheetId)).thenReturn(analysisDataList)

        val students = listOf(
            StudentModel(1L, "Student A"),
            StudentModel(2L, "Student B"),
        )
        `when`(worksheetFinder.assignedStudents(worksheetId)).thenReturn(students)

        // When
        val result = analyzeWorksheetUseCase(worksheetId)

        // Then
        assertEquals(worksheetId, result.worksheetId)
        assertEquals("test", result.worksheetName)
        assertEquals(2,result.completedEvaluationCount)
        assertEquals(2,result.studentTrainingResults.size)

        // Student A
        assertEquals(1,result.studentTrainingResults[0].studentId)
        assertEquals("Student A",result.studentTrainingResults[0].studentName)
        assertEquals(50,result.studentTrainingResults[0].correctWorksheetRate)
        assertEquals(true,result.studentTrainingResults[0].evaluated)

        // Student B
        assertEquals(2,result.studentTrainingResults[1].studentId)
        assertEquals("Student B",result.studentTrainingResults[1].studentName)
        assertEquals(0,result.studentTrainingResults[1].correctWorksheetRate)
        assertEquals(true,result.studentTrainingResults[1].evaluated)

        assertEquals(2,result.problemAnalysisResults.size)
        assertEquals(50,result.problemAnalysisResults[0].correctPerProblemRate)
        assertEquals(0,result.problemAnalysisResults[1].correctPerProblemRate)

    }

    @Test
    fun `올바른 학습지 분석 결과에서 채점하지 않은 학생에 대해서는 null을 반환하고 문제 정답률에 영향을 끼치지 않아야한다`() {
        // Given
        val worksheetId = 1L
        val teacher = Teacher("Teacher", 1)
        val worksheet = Worksheet("test", listOf(101L, 102L), teacher, worksheetId)
        `when`(worksheetRepository.findById(worksheetId)).thenReturn(worksheet)

        val analysisDataList = listOf(
            WorksheetAnalysisModel(studentId = 1L, problemEvaluations = listOf(true,false), correctCount = 1),
            WorksheetAnalysisModel(studentId = 2L, problemEvaluations = listOf(), correctCount = 0)
        )
        `when`(worksheetFinder.searchWorksheetAnalysisDataList(worksheetId)).thenReturn(analysisDataList)

        val students = listOf(
            StudentModel(1L, "Student A"),
            StudentModel(2L, "Student B"),
        )
        `when`(worksheetFinder.assignedStudents(worksheetId)).thenReturn(students)

        // When
        val result = analyzeWorksheetUseCase(worksheetId)

        // Then
        assertEquals(worksheetId, result.worksheetId)
        assertEquals("test", result.worksheetName)
        assertEquals(1,result.completedEvaluationCount)
        assertEquals(2,result.studentTrainingResults.size)

        // Student A
        assertEquals(1,result.studentTrainingResults[0].studentId)
        assertEquals("Student A",result.studentTrainingResults[0].studentName)
        assertEquals(50,result.studentTrainingResults[0].correctWorksheetRate)
        assertEquals(true,result.studentTrainingResults[0].evaluated)

        // Student B
        assertEquals(2,result.studentTrainingResults[1].studentId)
        assertEquals("Student B",result.studentTrainingResults[1].studentName)
        assertEquals(null,result.studentTrainingResults[1].correctWorksheetRate)
        assertEquals(false,result.studentTrainingResults[1].evaluated)

        assertEquals(2,result.problemAnalysisResults.size)
        assertEquals(100,result.problemAnalysisResults[0].correctPerProblemRate)
        assertEquals(0,result.problemAnalysisResults[1].correctPerProblemRate)
    }
}