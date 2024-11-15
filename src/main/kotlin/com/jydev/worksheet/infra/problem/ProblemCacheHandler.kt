package com.jydev.worksheet.infra.problem

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.jydev.worksheet.application.problem.model.ProblemModel
import com.jydev.worksheet.domain.problem.ProblemType
import com.jydev.worksheet.domain.problem.UnitCode
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class ProblemCacheHandler(
    private val redisProblemTemplate: RedisTemplate<String, ProblemCacheModel>
) {
    fun getProblemsFromCache(problemIds : List<Long>, searchFromDatabase : (problemIds : List<Long>) -> List<ProblemModel>) : List<ProblemModel> {
        val keys = problemIds.map { "problems::$it" }
        val cachedValues = redisProblemTemplate.opsForValue().multiGet(keys) ?: mutableListOf()
        val cachedProblemMap = problemIds.zip(cachedValues)
            .filter { (_, problem) -> problem != null }
            .associate { (id, problem) ->
                val problemModel = ProblemModel(
                    id = problem.id,
                    answer = problem.answer,
                    unitCode = UnitCode(problem.unitCode),
                    difficulty = problem.difficulty,
                    problemType = ProblemType.valueOf(problem.problemType)
                )
                id to problemModel
            }.toMutableMap()

        val missingProblemIds = problemIds.filterNot { it in cachedProblemMap }
        if(missingProblemIds.isNotEmpty()) {
            searchFromDatabase(missingProblemIds).also { problems ->
                updateCache(problems)
                problems.forEach { problem ->
                    cachedProblemMap[problem.id] = problem
                }
            }
        }

        setExpire(problemIds)

        return cachedProblemMap.values.toList()
    }

    private fun updateCache(problems : List<ProblemModel>) {
        val cacheMap = problems.map { problem ->
            ProblemCacheModel(
                id = problem.id,
                answer = problem.answer,
                unitCode = problem.unitCode.value(),
                difficulty = problem.difficulty,
                problemType = problem.problemType.name
            )
        }.associateBy { problem -> "problems::${problem.id}" }
        redisProblemTemplate.opsForValue().multiSet(cacheMap)
    }

    private fun setExpire(problemIds : List<Long>) {
        problemIds.map { problemId -> "problems::$problemId" }
            .forEach { key ->
                redisProblemTemplate.expire(key, CACHE_TTL, TimeUnit.DAYS)
            }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    data class ProblemCacheModel(
        val id : Long,
        val answer : String,
        val unitCode : String,
        val difficulty : Int,
        val problemType : String,
    )

    companion object {
        const val CACHE_TTL = 1L
    }
}