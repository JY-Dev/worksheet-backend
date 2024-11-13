package com.jydev.worksheet.presentation.worksheet

import com.jydev.worksheet.application.worksheet.CreateWorksheetUseCase
import com.jydev.worksheet.presentation.worksheet.model.request.CreateWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.response.CreateWorksheetResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class WorksheetApiController(
    private val createWorksheetUseCase: CreateWorksheetUseCase
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

}