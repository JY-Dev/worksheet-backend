package com.jydev.worksheet.infra.problem

import com.jydev.worksheet.application.problem.model.ProblemModel
import com.jydev.worksheet.domain.problem.ProblemType
import com.jydev.worksheet.domain.problem.UnitCode
import com.jydev.worksheet.infra.problem.ProblemCacheHandler
import com.jydev.worksheet.infra.problem.ProblemCacheHandler.ProblemCacheModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations

class ProblemCacheHandlerTest {
    
    private val redisTemplate: RedisTemplate<String, ProblemCacheModel> = mock()
    private val valueOperations: ValueOperations<String, ProblemCacheModel> = mock()

    init {
        whenever(redisTemplate.opsForValue()).thenReturn(valueOperations)
    }

    private val problemCacheHandler = ProblemCacheHandler(redisTemplate)

    @Test
    fun `캐시에 있는 경우 캐시에 없는 데이터만 디비 조회해야함`() {
        val problemId1 = 1L
        val problemId2 = 2L
        val cachedProblems = listOf(
            ProblemCacheModel(
                id = problemId1,
                answer = "1",
                unitCode = "uc1234",
                difficulty = 1,
                problemType = "SUBJECTIVE"
            ),
            ProblemCacheModel(
                id = problemId2,
                answer = "1",
                unitCode = "uc1234",
                difficulty = 1,
                problemType = "SUBJECTIVE"
            ),
        )

        `when`(valueOperations.multiGet(any())).thenReturn(cachedProblems)

        val searchFromDatabase: (List<Long>) -> List<ProblemModel> = { problemIds ->
            problemIds.map { problemId ->
                ProblemModel(
                    id = problemId,
                    answer = "1",
                    unitCode = UnitCode("uc1234"),
                    difficulty = 1,
                    problemType = ProblemType.SUBJECTIVE
                )
            }
        }

        val result = problemCacheHandler.getProblemsFromCache(
            listOf(problemId1, problemId2),
            searchFromDatabase
        )

        assertEquals(2, result.size)
        assertEquals(problemId1, result[0].id)
        assertEquals(problemId2, result[1].id)
    }

    @Test
    fun `캐시에 있는 경우 디비 조회 하지 않아야함`() {
        val problemId1 = 1L
        val problemId2 = 2L
        val cachedProblem = ProblemCacheModel(
            id = problemId1,
            answer = "1",
            unitCode = "uc1234",
            difficulty = 1,
            problemType = "SUBJECTIVE"
        )

        `when`(valueOperations.multiGet(any())).thenReturn(listOf(cachedProblem, null))

        val searchFromDatabase: (List<Long>) -> List<ProblemModel> = { problemIds ->
            problemIds.map { problemId ->
                ProblemModel(
                    id = problemId,
                    answer = "1",
                    unitCode = UnitCode("uc1234"),
                    difficulty = 1,
                    problemType = ProblemType.SUBJECTIVE
                )
            }
        }

        val result = problemCacheHandler.getProblemsFromCache(
            listOf(problemId1, problemId2),
            searchFromDatabase
        )

        assertEquals(2, result.size)
        assertEquals(problemId1, result[0].id)
        assertEquals(problemId2, result[1].id)
    }
}