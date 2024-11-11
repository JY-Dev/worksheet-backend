package com.jydev.worksheet.domain.problem

enum class ProblemLevel(private val description: String, val difficultyRange : IntRange) {
    HIGH("난이도 상", 5..5),
    MIDDLE("난이도 중", 2..4),
    LOW("난이도 하",1..1),
    ;
    companion object {
        fun fromDifficulty(difficulty: Int): ProblemLevel {
            return entries.find { it.difficultyRange.contains(difficulty) }
                ?: throw IllegalArgumentException("Difficulty not defined. difficulty: $difficulty")
        }
    }
}