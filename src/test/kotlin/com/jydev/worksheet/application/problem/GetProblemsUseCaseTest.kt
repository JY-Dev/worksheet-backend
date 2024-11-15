package com.jydev.worksheet.application.problem

import com.jydev.worksheet.application.problem.model.ProblemModel
import com.jydev.worksheet.application.problem.model.SearchCriteria
import com.jydev.worksheet.application.problem.model.SearchProblemType
import com.jydev.worksheet.domain.problem.ProblemLevel
import com.jydev.worksheet.domain.problem.ProblemType
import com.jydev.worksheet.domain.problem.UnitCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetProblemsUseCaseTest {

    private val finder: ProblemFinder = mock()

    @Test
    fun `난이도 MIDDLE에 대해 총 12개의 문제를 검색하고 올바르게 반환하는지 검증`() {
        val searchCriteria = SearchCriteria(
            totalSize = 12,
            level = ProblemLevel.MIDDLE,
            unitCodes = listOf(UnitCode("uc123")),
            searchProblemType = SearchProblemType.ALL
        )

        val mockProblemsLevelLow = List(3) { index ->
            ProblemModel(
                id = index.toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc123"),
                difficulty = 1,
                problemType = ProblemType.SELECTION
            )
        }
        val mockProblemsLevelMiddle = List(6) { index ->
            ProblemModel(
                id = (index + 3).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc123"),
                difficulty = 2,
                problemType = ProblemType.SELECTION
            )
        }
        val mockProblemsLevelHigh = List(3) { index ->
            ProblemModel(
                id = (index + 9).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc123"),
                difficulty = 5,
                problemType = ProblemType.SELECTION
            )
        }

        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 3, level = ProblemLevel.LOW)))
            .thenReturn(mockProblemsLevelLow)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 6, level = ProblemLevel.MIDDLE)))
            .thenReturn(mockProblemsLevelMiddle)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 3, level = ProblemLevel.HIGH)))
            .thenReturn(mockProblemsLevelHigh)

        val useCase = GetProblemsUseCase(finder)
        val result = useCase(searchCriteria)

        assertEquals(12, result.size)
        assertEquals(mockProblemsLevelHigh+ mockProblemsLevelMiddle + mockProblemsLevelLow, result)
    }

    @Test
    fun `난이도 HIGH에 대해 총 10개의 문제를 검색하고 올바르게 반환하는지 검증`() {
        val searchCriteria = SearchCriteria(
            totalSize = 10,
            level = ProblemLevel.HIGH,
            unitCodes = listOf(UnitCode("uc456")),
            searchProblemType = SearchProblemType.SELECTION
        )

        val mockProblemsLevelLow = List(2) { index ->
            ProblemModel(
                id = index.toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc456"),
                difficulty = 1,
                problemType = ProblemType.SELECTION
            )
        }
        val mockProblemsLevelMiddle = List(3) { index ->
            ProblemModel(
                id = (index + 2).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc456"),
                difficulty = 2,
                problemType = ProblemType.SELECTION
            )
        }
        val mockProblemsLevelHigh = List(5) { index ->
            ProblemModel(
                id = (index + 5).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc456"),
                difficulty = 5,
                problemType = ProblemType.SELECTION
            )
        }

        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 2, level = ProblemLevel.LOW)))
            .thenReturn(mockProblemsLevelLow)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 3, level = ProblemLevel.MIDDLE)))
            .thenReturn(mockProblemsLevelMiddle)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 5, level = ProblemLevel.HIGH)))
            .thenReturn(mockProblemsLevelHigh)

        val useCase = GetProblemsUseCase(finder)
        val result = useCase(searchCriteria)

        assertEquals(10, result.size)
        assertEquals(mockProblemsLevelHigh+ mockProblemsLevelMiddle + mockProblemsLevelLow, result)
    }

    @Test
    fun `난이도 LOW에 대해 총 10개의 문제를 검색하고 올바르게 반환하는지 검증`() {
        val searchCriteria = SearchCriteria(
            totalSize = 10,
            level = ProblemLevel.LOW,
            unitCodes = listOf(UnitCode("uc789")),
            searchProblemType = SearchProblemType.SUBJECTIVE
        )

        val mockProblemsLevelLow = List(5) { index ->
            ProblemModel(
                id = index.toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc789"),
                difficulty = 1,
                problemType = ProblemType.SUBJECTIVE
            )
        }
        val mockProblemsLevelMiddle = List(3) { index ->
            ProblemModel(
                id = (index + 8).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc789"),
                difficulty = 3,
                problemType = ProblemType.SUBJECTIVE
            )
        }
        val mockProblemsLevelHigh = List(2) { index ->
            ProblemModel(
                id = (index + 13).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc789"),
                difficulty = 5,
                problemType = ProblemType.SUBJECTIVE
            )
        }

        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 5, level = ProblemLevel.LOW)))
            .thenReturn(mockProblemsLevelLow)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 3, level = ProblemLevel.MIDDLE)))
            .thenReturn(mockProblemsLevelMiddle)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 2, level = ProblemLevel.HIGH)))
            .thenReturn(mockProblemsLevelHigh)

        val useCase = GetProblemsUseCase(finder)
        val result = useCase(searchCriteria)

        assertEquals(10, result.size)
        assertEquals(mockProblemsLevelHigh+ mockProblemsLevelMiddle + mockProblemsLevelLow, result)
    }

    @Test
    fun `올림 연산으로 인해 totalSize보다 많이 조회되더라도 총 문제 수가 totalSize로 제한되는지 검증`() {
        val searchCriteria = SearchCriteria(
            totalSize = 12,
            level = ProblemLevel.LOW,
            unitCodes = listOf(UnitCode("uc999")),
            searchProblemType = SearchProblemType.ALL
        )

        // 비율에 따라 문제 수가 계산될 때 올림 연산으로 인해 문제가 더 많이 조회되는 경우
        // 예: 12개의 문제에서 50%, 30%, 20%의 비율로 분배
        // 50% of 12 = 6, 30% of 12 = 3.6 반올림해서 4, 20% of 12 = 2.4 반올림해서 3 (총 10개로 정확히 떨어지지만 올림 연산 적용 시 더 많아질 수 있음)
        val mockProblemsLevelLow = List(6) { index -> // 올림 연산으로 5개가 아닌 6개 조회
            ProblemModel(
                id = index.toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc999"),
                difficulty = 1,
                problemType = ProblemType.SELECTION
            )
        }
        val mockProblemsLevelMiddle = List(4) { index ->
            ProblemModel(
                id = (index + 6).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc999"),
                difficulty = 3,
                problemType = ProblemType.SELECTION
            )
        }
        val mockProblemsLevelHigh = List(3) { index ->
            ProblemModel(
                id = (index + 10).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc999"),
                difficulty = 5,
                problemType = ProblemType.SELECTION
            )
        }

        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 6, level = ProblemLevel.LOW)))
            .thenReturn(mockProblemsLevelLow)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 4, level = ProblemLevel.MIDDLE)))
            .thenReturn(mockProblemsLevelMiddle)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 3, level = ProblemLevel.HIGH)))
            .thenReturn(mockProblemsLevelHigh)

        val useCase = GetProblemsUseCase(finder)
        val result = useCase(searchCriteria)

        assertEquals(12, result.size)
    }

    @Test
    fun `총 문제 수가 totalSize보다 작은 경우 검증`() {
        val searchCriteria = SearchCriteria(
            totalSize = 12,
            level = ProblemLevel.LOW,
            unitCodes = listOf(UnitCode("uc999")),
            searchProblemType = SearchProblemType.ALL
        )


        val mockProblemsLevelLow = List(4) { index ->
            ProblemModel(
                id = index.toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc999"),
                difficulty = 1,
                problemType = ProblemType.SELECTION
            )
        }
        val mockProblemsLevelMiddle = List(1) { index ->
            ProblemModel(
                id = (index + 6).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc999"),
                difficulty = 3,
                problemType = ProblemType.SELECTION
            )
        }
        val mockProblemsLevelHigh = List(1) { index ->
            ProblemModel(
                id = (index + 10).toLong(),
                answer = "Answer$index",
                unitCode = UnitCode("uc999"),
                difficulty = 5,
                problemType = ProblemType.SELECTION
            )
        }

        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 6, level = ProblemLevel.LOW)))
            .thenReturn(mockProblemsLevelLow)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 4, level = ProblemLevel.MIDDLE)))
            .thenReturn(mockProblemsLevelMiddle)
        whenever(finder.searchProblems(searchCriteria.copy(totalSize = 3, level = ProblemLevel.HIGH)))
            .thenReturn(mockProblemsLevelHigh)

        val useCase = GetProblemsUseCase(finder)
        val result = useCase.invoke(searchCriteria)

        assertEquals(6, result.size)
    }
}
