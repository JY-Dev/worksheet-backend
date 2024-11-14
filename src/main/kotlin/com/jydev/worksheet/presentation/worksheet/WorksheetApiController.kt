package com.jydev.worksheet.presentation.worksheet

import com.jydev.worksheet.application.worksheet.AssignWorksheetUseCase
import com.jydev.worksheet.application.worksheet.CreateWorksheetUseCase
import com.jydev.worksheet.application.worksheet.EvaluateWorksheetUseCase
import com.jydev.worksheet.application.worksheet.GetWorksheetProblemsUseCase
import com.jydev.worksheet.domain.worksheet.evaluation.SubmittedAnswer
import com.jydev.worksheet.presentation.worksheet.model.request.AssignWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.request.CreateWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.request.EvaluateWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.response.AssignWorksheetResponse
import com.jydev.worksheet.presentation.worksheet.model.response.CreateWorksheetResponse
import com.jydev.worksheet.presentation.worksheet.model.response.EvaluateWorksheetResponse
import com.jydev.worksheet.presentation.worksheet.model.response.GetWorksheetProblemsResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class WorksheetApiController(
    private val createWorksheetUseCase: CreateWorksheetUseCase,
    private val assignedWorksheetUseCase: AssignWorksheetUseCase,
    private val getWorksheetProblemsUseCase: GetWorksheetProblemsUseCase,
    private val evaluateWorksheetUseCase: EvaluateWorksheetUseCase,
) : WorksheetApi {

    override fun createWorksheet(request: CreateWorksheetRequest): CreateWorksheetResponse {
        val model = createWorksheetUseCase(
            teacherId = request.teacherId,
            problemIds = request.problemIds,
            worksheetName = request.worksheetName,
        )

        return CreateWorksheetResponse(
            worksheetId = model.worksheetId,
            teacherId = model.creatorId,
            worksheetName = model.name,
            problemIds = model.problemIds
        )
    }

    override fun assignWorksheet(studentIds: List<Long>, request: AssignWorksheetRequest) : AssignWorksheetResponse {
        val assignedStudentIds = assignedWorksheetUseCase(
            teacherId = request.teacherId,
            worksheetId = request.worksheetId,
            studentIds = studentIds
        )

        return AssignWorksheetResponse(assignedStudentIds = assignedStudentIds)
    }

    override fun getWorksheetProblems(worksheetId: Long): GetWorksheetProblemsResponse {
        val items = getWorksheetProblemsUseCase(worksheetId).map { worksheetProblem ->
            GetWorksheetProblemsResponse.GetWorksheetProblemsItemResponse(
                problemId = worksheetProblem.problemId,
                unitCode = worksheetProblem.unitCode.value(),
                level = worksheetProblem.difficulty,
                problemType = worksheetProblem.problemType
            )
        }

        return GetWorksheetProblemsResponse(items)
    }

    override fun evaluateWorksheet(worksheetId: Long, request: EvaluateWorksheetRequest): EvaluateWorksheetResponse {

        val submittedAnswers = request.submittedAnswers.map { answer ->
            SubmittedAnswer(
                problemId = answer.problemId,
                answer = answer.submittedAnswer
            )
        }

        val items = evaluateWorksheetUseCase(
            studentId = request.studentId,
            worksheetId = worksheetId,
            answers = submittedAnswers
        ).map { (problemId, collect) ->
            EvaluateWorksheetResponse.EvaluateWorksheetItemResponse(
                problemId = problemId,
                collect = collect
            )
        }

        return EvaluateWorksheetResponse(items)
    }

}