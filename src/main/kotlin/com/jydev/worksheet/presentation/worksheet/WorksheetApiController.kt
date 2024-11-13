package com.jydev.worksheet.presentation.worksheet

import com.jydev.worksheet.application.worksheet.AssignWorksheetUseCase
import com.jydev.worksheet.application.worksheet.CreateWorksheetUseCase
import com.jydev.worksheet.presentation.worksheet.model.request.AssignWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.request.CreateWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.response.AssignWorksheetResponse
import com.jydev.worksheet.presentation.worksheet.model.response.CreateWorksheetResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class WorksheetApiController(
    private val createWorksheetUseCase: CreateWorksheetUseCase,
    private val assignedWorksheetUseCase: AssignWorksheetUseCase,
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

}