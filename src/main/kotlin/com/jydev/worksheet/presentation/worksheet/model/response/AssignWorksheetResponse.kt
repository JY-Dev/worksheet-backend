package com.jydev.worksheet.presentation.worksheet.model.response

import io.swagger.v3.oas.annotations.media.Schema

data class AssignWorksheetResponse(

    @Schema(
        description = """
            Assign된 학생 id 목록
            """,
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val assignedStudentIds : List<Long>
)
