package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.worksheet.model.WorksheetAnalysisResultModel
import com.jydev.worksheet.domain.worksheet.WorksheetRepository
import org.springframework.stereotype.Service

@Service
class AnalyzeWorksheetUseCase(
    private val worksheetRepository: WorksheetRepository,
    private val worksheetFinder: WorksheetFinder,
) {

    operator fun invoke(worksheetId: Long): WorksheetAnalysisResultModel {
        val worksheet = worksheetRepository.findById(worksheetId)
            ?: throw NoSuchElementException("Worksheet with ID $worksheetId does not exist")
        val problemIdMap = worksheet.problemIds.mapIndexed { index, problemId ->
            index to problemId
        }.toMap()

        val analysisDataList = worksheetFinder.searchWorksheetAnalysisDataList(worksheetId)
            .filter { analysis -> analysis.problemEvaluations.isNotEmpty() }

        val problemCount = problemIdMap.size
        val studentWorksheetCorrectRateMap = analysisDataList.associate { analysisData ->
            val correctRate = analysisData.correctCount.toDouble() / problemCount
            analysisData.studentId to (correctRate * 100).toInt()
        }
        val studentTrainingResults = worksheetFinder.assignedStudents(worksheetId)
            .map { student ->
                WorksheetAnalysisResultModel.StudentTrainingResult(
                    studentId = student.id,
                    studentName = student.name,
                    evaluated = studentWorksheetCorrectRateMap[student.id] != null,
                    correctWorksheetRate = studentWorksheetCorrectRateMap[student.id]
                )
            }

        val completedEvaluationCount = analysisDataList.size
        val problemAnalysisResults = problemIdMap.map { (index, problemId) ->
            val problemCollectCount = analysisDataList.map { analysisData -> analysisData.problemEvaluations[index] }
                .count { correct -> correct }

            val correctRate =
                if (completedEvaluationCount != 0) problemCollectCount.toDouble() / completedEvaluationCount else null
            WorksheetAnalysisResultModel.ProblemAnalysisResult(
                problemId = problemId,
                correctPerProblemRate = correctRate?.let { rate ->
                    (rate * 100).toInt()
                }
            )
        }.toList()

        return WorksheetAnalysisResultModel(
            worksheetId = worksheet.id!!,
            worksheetName = worksheet.name,
            completedEvaluationCount = completedEvaluationCount,
            studentTrainingResults = studentTrainingResults,
            problemAnalysisResults = problemAnalysisResults
        )
    }
}