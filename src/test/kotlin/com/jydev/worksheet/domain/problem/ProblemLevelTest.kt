package com.jydev.worksheet.domain.problem

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ProblemLevelTest {

    @ParameterizedTest(name = "난이도 {0}일 때 {1}를 반환")
    @CsvSource(
        "1, LOW",
        "2, MIDDLE",
        "3, MIDDLE",
        "4, MIDDLE",
        "5, HIGH"
    )
    fun `난이도에 따른 ProblemLevel 반환`(difficulty: Int, expectedLevel: ProblemLevel) {
        val result = ProblemLevel.fromDifficulty(difficulty)
        assertEquals(expectedLevel, result)
    }

    @ParameterizedTest(name = "정의되지 않은 난이도 {0}일 경우 예외")
    @CsvSource("0, 6, 10")
    fun `정의되지 않은 난이도 예외 발생`(difficulty: Int) {
        assertThrows<IllegalArgumentException> {
            ProblemLevel.fromDifficulty(difficulty)
        }
    }
}