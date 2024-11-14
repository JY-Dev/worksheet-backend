package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.problem.ProblemFinder
import com.jydev.worksheet.application.worksheet.model.WorksheetModel
import com.jydev.worksheet.domain.worksheet.Worksheet
import com.jydev.worksheet.domain.worksheet.WorksheetRepository
import com.jydev.worksheet.domain.worksheet.user.Teacher
import com.jydev.worksheet.domain.worksheet.user.TeacherRepository
import com.jydev.worksheet.application.worksheet.error.DuplicateWorksheetNameException
import com.jydev.worksheet.application.worksheet.error.InvalidProblemContainException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.transaction.support.TransactionTemplate

class CreateWorksheetUseCaseTest {

    private val transactionTemplate = mock(TransactionTemplate::class.java)
    private val problemFinder = mock(ProblemFinder::class.java)
    private val worksheetRepository = mock(WorksheetRepository::class.java)
    private val teacherRepository = mock(TeacherRepository::class.java)
    private val createWorksheetUseCase = CreateWorksheetUseCase(
        transactionTemplate, problemFinder, worksheetRepository, teacherRepository
    )

    @Test
    fun `존재하지 않는 선생님 ID로 요청할 경우 예외가 발생`() {
        // given
        val teacherId = 1L
        `when`(teacherRepository.findById(teacherId)).thenReturn(null)

        // when & then
        assertThrows<NoSuchElementException> {
            createWorksheetUseCase(teacherId, listOf(1L, 2L, 3L), "New Worksheet")
        }
    }

    @Test
    fun `중복된 학습지 이름으로 요청할 경우 예외가 발생`() {
        // given
        val teacherId = 1L
        val teacher = Teacher("John Doe", id = teacherId)
        val worksheetName = "Duplicate Name"
        `when`(teacherRepository.findById(teacherId)).thenReturn(teacher)
        `when`(worksheetRepository.existsByCreatorIdAndName(teacherId, worksheetName)).thenReturn(true)

        // when & then
        assertThrows<DuplicateWorksheetNameException> {
            createWorksheetUseCase(teacherId, listOf(1L, 2L, 3L), worksheetName)
        }
    }

    @Test
    fun `존재하지 않는 문제 ID가 포함된 경우 예외가 발생`() {
        // given
        val teacherId = 1L
        val teacher = Teacher("John Doe", id = teacherId)
        val problemIds = listOf(1L, 2L, 3L)
        val worksheetName = "Valid Worksheet"
        `when`(teacherRepository.findById(teacherId)).thenReturn(teacher)
        `when`(worksheetRepository.existsByCreatorIdAndName(teacherId, worksheetName)).thenReturn(false)
        `when`(problemFinder.countExistingProblems(problemIds)).thenReturn(2) // 하나의 문제 ID가 유효하지 않음

        // when & then
        assertThrows<InvalidProblemContainException> {
            createWorksheetUseCase(teacherId, problemIds, worksheetName)
        }
    }

    @Test
    fun `유효한 요청일 경우 학습지가 성공적으로 생성 검증`() {
        // given
        val teacherId = 1L
        val teacher = Teacher("John Doe", id = teacherId)
        val problemIds = listOf(1L, 2L, 3L)
        val worksheetName = "Valid Worksheet"
        val worksheet = Worksheet(worksheetName, problemIds, teacher)
        val worksheetModel = WorksheetModel(1L,worksheetName, teacherId, problemIds)
        `when`(teacherRepository.findById(teacherId)).thenReturn(teacher)
        `when`(worksheetRepository.existsByCreatorIdAndName(teacherId, worksheetName)).thenReturn(false)
        `when`(problemFinder.countExistingProblems(problemIds)).thenReturn(3)
        `when`(worksheetRepository.save(worksheet)).thenReturn(worksheet)
        `when`(transactionTemplate.execute<WorksheetModel>(any())).thenReturn(worksheetModel)

        // when
        val result = createWorksheetUseCase(teacherId, problemIds, worksheetName)

        // then
        assertNotNull(result)
        assertEquals(worksheetName, result.name)
        assertEquals(teacherId, result.creatorId)
        assertEquals(problemIds, result.problemIds)
    }
}