package com.jydev.worksheet.domain.problem

interface ProblemRepository {
    fun save(problem: Problem) : Problem
    fun delete(problem: Problem)
    fun findById(id: Long): Problem?
}