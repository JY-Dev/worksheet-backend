package com.jydev.worksheet.presentation.problem

import com.jydev.worksheet.application.problem.model.SearchProblemType
import com.jydev.worksheet.domain.problem.ProblemLevel
import com.jydev.worksheet.presentation.problem.model.response.GetProblemsResponse
import com.mindshare.api.core.web.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(
    name = "Problem API",
    description = """
        문제 관련 API 입니다."""
)
@RequestMapping("/problems")
interface ProblemApi {

    @Operation(
        summary = "문제 검색 API",
        description = """
                    문제를 검색 합니다."""
    )
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "정상"), ApiResponse(
            responseCode = "400", description = """
                            * 에러코드 
                            
                            - A05001 : 요청한 데이터가 요구사항을 충족하지 않습니다.
                            """,
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @GetMapping(produces = [APPLICATION_JSON_VALUE])
    fun getProblems(

        @Schema(
            description = """
            총 문제 수
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @RequestParam(required = true) totalCount: Int,

        @Schema(
            description = """
            문제 난이도
            HIGH[난이도 상]
            MIDDLE[난이도 중]
            LOW[난이도 하]
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @RequestParam(required = true) level: ProblemLevel,

        @Schema(
            description = """
            문제 종류
            ALL("전체 조회"),
            SUBJECTIVE("주관식"),
            SELECTION("객관식");
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @RequestParam(required = true) problemType: SearchProblemType,

        @Schema(
            description = """
            문제 유형 코드 목록
            """,
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        @RequestParam(required = false) unitCodeList: List<String> = emptyList(),

        ): GetProblemsResponse
}