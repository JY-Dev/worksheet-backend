package com.jydev.worksheet.application.problem

import com.jydev.worksheet.domain.problem.ProblemLevel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

class ProblemCountCalculatorTest {

    @ParameterizedTest(name = "난이도 {0}일 때 총 {1}개의 문제 중 LOW 문제 수는 {2}개 반환")
    @CsvSource(
        "HIGH, 50, 10",  // HIGH 난이도에서 LOW 문제는 50개의 20%
        "MIDDLE, 40, 10",  // MIDDLE 난이도에서 LOW 문제는 40개의 25%
        "LOW, 30, 15"  // LOW 난이도에서 LOW 문제는 30개의 50%
    )
    fun `LOW 문제 수 계산`(level: ProblemLevel, totalSize: Int, expectedCount: Int) {
        val calculator = ProblemCountCalculator.fromProblemLevel(level)
        val actualCount = calculator.calculateProblemCount(totalSize, ProblemLevel.LOW)
        assertEquals(expectedCount, actualCount)
    }

    @ParameterizedTest(name = "난이도 {0}일 때 총 {1}개의 문제 중 MIDDLE 문제 수는 {2}개 반환")
    @CsvSource(
        "HIGH, 50, 15",  // HIGH 난이도에서 MIDDLE 문제는 50개의 30%
        "MIDDLE, 40, 20",  // MIDDLE 난이도에서 MIDDLE 문제는 40개의 50%
        "LOW, 30, 9"  // LOW 난이도에서 MIDDLE 문제는 30개의 30%
    )
    fun `MIDDLE 문제 수 계산`(level: ProblemLevel, totalSize: Int, expectedCount: Int) {
        val calculator = ProblemCountCalculator.fromProblemLevel(level)
        val actualCount = calculator.calculateProblemCount(totalSize, ProblemLevel.MIDDLE)
        assertEquals(expectedCount, actualCount)
    }

    @ParameterizedTest(name = "난이도 {0}일 때 총 {1}개의 문제 중 HIGH 문제 수는 {2}개 반환")
    @CsvSource(
        "HIGH, 50, 25",  // HIGH 난이도에서 HIGH 문제는 50개의 50%
        "MIDDLE, 40, 10",  // MIDDLE 난이도에서 HIGH 문제는 40개의 25%
        "LOW, 30, 6"  // LOW 난이도에서 HIGH 문제는 30개의 20%
    )
    fun `HIGH 문제 수 계산`(level: ProblemLevel, totalSize: Int, expectedCount: Int) {
        val calculator = ProblemCountCalculator.fromProblemLevel(level)
        val actualCount = calculator.calculateProblemCount(totalSize, ProblemLevel.HIGH)
        assertEquals(expectedCount, actualCount)
    }

    @Test
    fun `소수점 계산 결과가 올림 처리되는지 검증 - LOW 문제 수`() {
        val calculator = ProblemCountCalculator.HIGH

        // 49 * 0.2 = 9.8 → 올림 처리되어 10
        val lowCount = calculator.calculateProblemCount(49, ProblemLevel.LOW)
        assertEquals(10, lowCount, "LOW 문제 수가 올림 처리되지 않았습니다")
    }

    @Test
    fun `소수점 계산 결과가 올림 처리되는지 검증 - MIDDLE 문제 수`() {
        val calculator = ProblemCountCalculator.HIGH

        // 49 * 0.3 = 14.7 → 올림 처리되어 15
        val middleCount = calculator.calculateProblemCount(49, ProblemLevel.MIDDLE)
        assertEquals(15, middleCount, "MIDDLE 문제 수가 올림 처리되지 않았습니다")
    }

    @Test
    fun `소수점 계산 결과가 올림 처리되는지 검증 - HIGH 문제 수`() {
        val calculator = ProblemCountCalculator.HIGH

        // 39 * 0.5 = 19.5 → 올림 처리되어 20
        val highCount = calculator.calculateProblemCount(39, ProblemLevel.HIGH)
        assertEquals(20, highCount, "HIGH 문제 수가 올림 처리되지 않았습니다")
    }
}