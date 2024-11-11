package com.jydev.worksheet.application.problem

import com.jydev.worksheet.domain.problem.ProblemLevel
import kotlin.math.ceil

enum class ProblemCountCalculator(
    val calculateProblemCount: (totalSize: Int, level: ProblemLevel) -> Int
) {
    HIGH({ totalSize, level ->
        when (level) {
            ProblemLevel.LOW -> ceil(totalSize * 0.2).toInt()
            ProblemLevel.MIDDLE -> ceil(totalSize * 0.3).toInt()
            ProblemLevel.HIGH -> ceil(totalSize * 0.5).toInt()
        }
    }),
    MIDDLE({ totalSize, level ->
        when (level) {
            ProblemLevel.LOW -> ceil(totalSize * 0.25).toInt()
            ProblemLevel.MIDDLE -> ceil(totalSize * 0.5).toInt()
            ProblemLevel.HIGH -> ceil(totalSize * 0.25).toInt()
        }
    }),
    LOW({ totalSize, level ->
        when (level) {
            ProblemLevel.LOW -> ceil(totalSize * 0.5).toInt()
            ProblemLevel.MIDDLE -> ceil(totalSize * 0.3).toInt()
            ProblemLevel.HIGH -> ceil(totalSize * 0.2).toInt()
        }
    });

    companion object {

        fun fromProblemLevel(level : ProblemLevel) : ProblemCountCalculator {
            return when(level) {
                ProblemLevel.HIGH -> HIGH
                ProblemLevel.MIDDLE -> MIDDLE
                ProblemLevel.LOW -> LOW
            }
        }
    }
}