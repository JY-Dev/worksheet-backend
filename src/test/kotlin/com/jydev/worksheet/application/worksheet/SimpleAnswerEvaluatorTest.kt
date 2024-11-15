package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.problem.ProblemFinder
import com.jydev.worksheet.application.problem.model.ProblemModel
import com.jydev.worksheet.domain.problem.ProblemType
import com.jydev.worksheet.domain.problem.UnitCode
import com.jydev.worksheet.domain.worksheet.evaluation.EvaluatedAnswer
import com.jydev.worksheet.domain.worksheet.evaluation.SubmittedAnswer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

class SimpleAnswerEvaluatorTest {
    private val problemFinder: ProblemFinder = mock()
    private val answerEvaluator = SimpleAnswerEvaluator(problemFinder)

    @Test
    fun `답안을 올바르게 평가한다`() {
        // given
        val submittedAnswers = listOf(
            SubmittedAnswer(1, "1"),
            SubmittedAnswer(2, "1")
        )
        val problems = listOf(
            ProblemModel(
                unitCode = UnitCode("uc1"),
                problemType = ProblemType.SELECTION,
                difficulty = 1,
                answer = "1",
                id = 1
            ),
            ProblemModel(
                unitCode = UnitCode("uc1"),
                problemType = ProblemType.SELECTION,
                difficulty = 1,
                answer = "2",
                id = 2
            ),
        )
        `when`(problemFinder.searchProblems(listOf(1, 2))).thenReturn(problems)

        // when
        val evaluatedAnswers: List<EvaluatedAnswer> = answerEvaluator.evaluate(submittedAnswers)

        // then
        assertEquals(2, evaluatedAnswers.size)
        assertEquals(true, evaluatedAnswers[0].correct)
        assertEquals(false, evaluatedAnswers[1].correct)
        assertEquals("1", evaluatedAnswers[0].submittedAnswer)
        assertEquals("1", evaluatedAnswers[1].submittedAnswer)
        assertEquals("1", evaluatedAnswers[0].expectedAnswer)
        assertEquals("2", evaluatedAnswers[1].expectedAnswer)
    }

    @Test
    fun `평가된 답안이 제출한 정답 순서대로 정렬된다`() {
        // given
        val submittedAnswers = listOf(
            SubmittedAnswer(2, "1"),
            SubmittedAnswer(1, "2")
        )
        val problems = listOf(
            ProblemModel(
                unitCode = UnitCode("uc1"),
                problemType = ProblemType.SELECTION,
                difficulty = 1,
                answer = "1",
                id = 1
            ),
            ProblemModel(
                unitCode = UnitCode("uc1"),
                problemType = ProblemType.SELECTION,
                difficulty = 1,
                answer = "1",
                id = 2
            ),
        )
        `when`(problemFinder.searchProblems(listOf(2, 1))).thenReturn(problems)

        // when
        val evaluatedAnswers: List<EvaluatedAnswer> = answerEvaluator.evaluate(submittedAnswers)

        // then
        assertEquals(2, evaluatedAnswers.size)
        assertEquals(2, evaluatedAnswers[0].problemId)
        assertEquals(1, evaluatedAnswers[1].problemId)
        assertEquals(true, evaluatedAnswers[0].correct)
        assertEquals(false, evaluatedAnswers[1].correct)
    }
}