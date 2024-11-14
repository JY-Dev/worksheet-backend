package com.jydev.worksheet.domain.worksheet.evaluation

import com.jydev.worksheet.domain.worksheet.AssignedWorksheet
import com.jydev.worksheet.domain.worksheet.Worksheet
import com.jydev.worksheet.domain.worksheet.user.Student
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

class AssignedWorksheetTest {

    private val worksheet = Mockito.mock(Worksheet::class.java)
    private val student = Mockito.mock(Student::class.java)
    private val answerEvaluator = Mockito.mock(AnswerEvaluator::class.java)
    private val assignedWorksheet = AssignedWorksheet(worksheet, student)

    private val submittedAnswers = listOf(
        SubmittedAnswer(1L, "1"),
        SubmittedAnswer(2L, "2"),
        SubmittedAnswer(3L, "3"),
        SubmittedAnswer(4L, "4")
    )

    // 평가된 답안 리스트를 모의 설정합니다.
    private val evaluatedAnswers = listOf(
        EvaluatedAnswer(1L, true, "1", "1"),
        EvaluatedAnswer(2L, false, "2", "1"),
        EvaluatedAnswer(3L, true, "1", "1"),
        EvaluatedAnswer(4L, true, "1", "1")
    )

    private val problemIds = listOf(1L, 2L, 3L, 4L)


    @Test
    fun `평가가 올바르게 수행되는지 검증`() {
        Mockito.`when`(worksheet.problemIds).thenReturn(problemIds)
        Mockito.`when`(answerEvaluator.evaluate(submittedAnswers)).thenReturn(evaluatedAnswers)

        assignedWorksheet.evaluate(answerEvaluator, submittedAnswers)

        // then: 평가 결과가 올바르게 설정되었는지 검증합니다.
        val evaluationResult = assignedWorksheet.evaluationResult
        assertNotNull(evaluationResult, "평가 결과가 설정되어 있어야 합니다.")
        assertEquals(assignedWorksheet.submittedAnswers, submittedAnswers, "제출한 답안이 올바르게 설정되야 합니다.")
        assertEquals(3, evaluationResult.correctCount, "정답이 올바르게 계산되어야 합니다.")
    }

    @Test
    fun `이미 평가된 학습지인지 검증`() {
        Mockito.`when`(worksheet.problemIds).thenReturn(problemIds)
        Mockito.`when`(answerEvaluator.evaluate(submittedAnswers)).thenReturn(evaluatedAnswers)

        assignedWorksheet.evaluate(answerEvaluator, submittedAnswers)

        // when: isAlreadyEvaluated 메서드를 호출합니다.
        val result = assignedWorksheet.isAlreadyEvaluated()

        // then: 이미 평가된 것으로 확인되어야 합니다.
        assertTrue(result, "평가가 이미 수행된 경우 true를 반환해야 합니다.")
    }

    @Test
    fun `제출된 답안의 크기가 문제 크기와 일치하지 않을 때 예외가 발생해야 함`() {


        val invalidSubmittedAnswer = listOf(
            SubmittedAnswer(1001L, "Answer 1"),
            SubmittedAnswer(1002L, "Answer 2") // 문제 크기보다 하나 적음
        )

        Mockito.`when`(worksheet.problemIds).thenReturn(problemIds)
        Mockito.`when`(answerEvaluator.evaluate(invalidSubmittedAnswer)).thenReturn(evaluatedAnswers)


        // when & then
        assertThrows<IllegalArgumentException> {
            assignedWorksheet.evaluate(answerEvaluator, invalidSubmittedAnswer)
        }
    }

    @Test
    fun `제출된 답안의 문제 ID가 순서에 맞지 않을 때 예외가 발생해야 함`() {

        val invalidSubmittedAnswer = listOf(
            SubmittedAnswer(1L, "Answer 1"),
            SubmittedAnswer(2L, "Answer 2"),
            SubmittedAnswer(4L, "Answer 3"),
            SubmittedAnswer(3L, "Answer 4"),
        )

        Mockito.`when`(worksheet.problemIds).thenReturn(problemIds)
        Mockito.`when`(answerEvaluator.evaluate(invalidSubmittedAnswer)).thenReturn(evaluatedAnswers)

        // when & then
        assertThrows<IllegalArgumentException> {
            assignedWorksheet.evaluate(answerEvaluator, invalidSubmittedAnswer)
        }
    }
}