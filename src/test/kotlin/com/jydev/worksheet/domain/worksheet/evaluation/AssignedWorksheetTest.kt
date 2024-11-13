package com.jydev.worksheet.domain.worksheet.evaluation

import com.jydev.worksheet.domain.worksheet.AssignedWorksheet
import com.jydev.worksheet.domain.worksheet.Worksheet
import com.jydev.worksheet.domain.worksheet.user.Student
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class AssignedWorksheetTest {

    @Test
    fun `평가가 올바르게 수행되는지 검증`() {
        // given: 가짜 Worksheet와 Student 객체를 생성합니다.
        val worksheet = Mockito.mock(Worksheet::class.java)
        val student = Mockito.mock(Student::class.java)
        val answerEvaluator = Mockito.mock(AnswerEvaluator::class.java)

        // 문제 ID 리스트를 모의 설정합니다.
        val problemIds = listOf(1L, 2L, 3L,4L)
        Mockito.`when`(worksheet.problemIds).thenReturn(problemIds)

        // 제출된 답안 리스트를 설정합니다.
        val submittedAnswers = listOf(
            SubmittedAnswer(1L, "1"),
            SubmittedAnswer(2L, "2"),
            SubmittedAnswer(3L, "3"),
            SubmittedAnswer(4L, "4")
        )

        // 평가된 답안 리스트를 모의 설정합니다.
        val evaluatedAnswers = listOf(
            EvaluatedAnswer(1L, true, "1", "1"),
            EvaluatedAnswer(2L, false, "2", "1"),
            EvaluatedAnswer(3L, true, "1", "1"),
            EvaluatedAnswer(4L, true, "1", "1")
        )
        Mockito.`when`(answerEvaluator.evaluate(submittedAnswers)).thenReturn(evaluatedAnswers)

        // when: AssignedWorksheet 객체를 생성하고 평가를 수행합니다.
        val assignedWorksheet = AssignedWorksheet(worksheet, student)
        assignedWorksheet.evaluate(answerEvaluator, submittedAnswers)

        // then: 평가 결과가 올바르게 설정되었는지 검증합니다.
        val evaluationResult = assignedWorksheet.evaluationResult
        assertNotNull(evaluationResult, "평가 결과가 설정되어 있어야 합니다.")
        assertEquals(assignedWorksheet.submittedAnswers, submittedAnswers, "제출한 답안이 올바르게 설정되야 합니다.")
        assertEquals(3, evaluationResult.correctCount, "정답이 올바르게 계산되어야 합니다.")
    }

    @Test
    fun `이미 평가된 학습지인지 검증`() {
        // given: 가짜 Worksheet와 Student 객체를 생성합니다.
        val worksheet = Mockito.mock(Worksheet::class.java)
        val student = Mockito.mock(Student::class.java)
        val answerEvaluator = Mockito.mock(AnswerEvaluator::class.java)

        // 문제 ID 리스트를 모의 설정합니다.
        val problemIds = listOf(1L, 2L, 3L,4L)
        Mockito.`when`(worksheet.problemIds).thenReturn(problemIds)

        // 제출된 답안 리스트를 설정합니다.
        val submittedAnswers = listOf(
            SubmittedAnswer(1L, "1"),
            SubmittedAnswer(2L, "2"),
            SubmittedAnswer(3L, "3"),
            SubmittedAnswer(4L, "4")
        )

        // 평가된 답안 리스트를 모의 설정합니다.
        val evaluatedAnswers = listOf(
            EvaluatedAnswer(1L, true, "1", "1"),
            EvaluatedAnswer(2L, false, "2", "1"),
            EvaluatedAnswer(3L, true, "1", "1"),
            EvaluatedAnswer(4L, true, "1", "1")
        )
        Mockito.`when`(answerEvaluator.evaluate(submittedAnswers)).thenReturn(evaluatedAnswers)

        // when: AssignedWorksheet 객체를 생성하고 평가를 수행합니다.
        val assignedWorksheet = AssignedWorksheet(worksheet, student)
        assignedWorksheet.evaluate(answerEvaluator, submittedAnswers)

        // when: isAlreadyEvaluated 메서드를 호출합니다.
        val result = assignedWorksheet.isAlreadyEvaluated()

        // then: 이미 평가된 것으로 확인되어야 합니다.
        assertTrue(result, "평가가 이미 수행된 경우 true를 반환해야 합니다.")
    }
}