package com.jydev.worksheet.presentation.worksheet

import com.jydev.worksheet.presentation.worksheet.model.request.AssignWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.request.CreateWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.request.EvaluateWorksheetRequest
import com.jydev.worksheet.presentation.worksheet.model.response.*
import com.mindshare.api.core.web.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

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
                            - A01002 : Invalid 문제 포함
                            - A05001 : 요청한 데이터가 요구사항을 충족하지 않습니다.
                            """,
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = [APPLICATION_JSON_VALUE])
    fun createWorksheet(@RequestBody @Valid request: CreateWorksheetRequest): CreateWorksheetResponse

    @Operation(
        summary = "학습지 출제 API",
        description = """
                    학습지를 학생에게 출제 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "정상"),
        ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{pieceId}", produces = [APPLICATION_JSON_VALUE], params = ["studentIds"])
    fun assignWorksheet(

        @Schema(
            description = """
            문제지 ID
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @PathVariable("pieceId") worksheetId: Long,

        @Schema(
            description = """
            학생 id 목록
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @RequestParam(required = true) @Valid
        @Size(min = 1, message = "The list must contain at least 1 student ID")
        studentIds: List<Long>,

        @RequestBody @Valid request: AssignWorksheetRequest
    ): AssignWorksheetResponse

    @Operation(
        summary = "학습지 문제 조회 API",
        description = """
                    학습지의 문제를 조회 합니다.
                    """
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상")
    )
    @GetMapping("/problems", produces = [APPLICATION_JSON_VALUE])
    fun getWorksheetProblems(
        @Schema(
            description = """
            학습지 Id
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @RequestParam(required = true, name = "pieceId") worksheetId: Long
    ): GetWorksheetProblemsResponse

    @Operation(
        summary = "학습지 채점 API",
        description = """
                    학습지의 문제를 채점 합니다.
                    """
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상"),
        ApiResponse(
            responseCode = "400", description = """
                            * 에러코드 
                            - A01003 : 이미 채점된 학습지
                            """
        )
    )
    @PutMapping("/problems", produces = [APPLICATION_JSON_VALUE])
    fun evaluateWorksheet(
        @Schema(
            description = """
            학습지 Id
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @RequestParam(required = true, name = "pieceId") worksheetId: Long,
        @RequestBody @Valid request : EvaluateWorksheetRequest
    ): EvaluateWorksheetResponse

    @Operation(
        summary = "학습지 학습 통계 분석 API",
        description = """
                    학습지 학습 통계를 분석 합니다.
                    """
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "정상"),
        ApiResponse(
            responseCode = "400", description = """
                            * 에러코드 
                            - A01003 : 이미 채점된 학습지
                            """
        )
    )
    @GetMapping("/analyze", produces = [APPLICATION_JSON_VALUE])
    fun analyzeWorksheet(
        @Schema(
            description = """
            학습지 Id
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @RequestParam(required = true, name = "pieceId") worksheetId: Long
    ) : AnalyzeWorksheetResponse
}