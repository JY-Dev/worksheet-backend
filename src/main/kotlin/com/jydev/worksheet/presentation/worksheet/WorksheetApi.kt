package com.jydev.worksheet.presentation.worksheet

import com.jydev.worksheet.presentation.worksheet.model.request.CreateWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.response.CreateWorksheetResponse
import com.mindshare.api.core.web.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(
    name = "Worksheet API",
    description = """
        학습지 관련 API 입니다."""
)
@RequestMapping("/piece")
interface WorksheetApi {

    @Operation(
        summary = "학습지 생성 API",
        description = """
                    학습지를 생성 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "정상"), ApiResponse(
            responseCode = "400", description = """
                            * 에러코드 
                            
                            - A01001 : 학습지 이름 중복
                            - A01002 : Invalid 문제 포함된 경우
                            - A05001 : 요청한 데이터가 요구사항을 충족하지 않습니다.
                            """,
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = [APPLICATION_JSON_VALUE])
    fun createWorksheet(@RequestBody @Valid request: CreateWorksheetRequest): CreateWorksheetResponse
}