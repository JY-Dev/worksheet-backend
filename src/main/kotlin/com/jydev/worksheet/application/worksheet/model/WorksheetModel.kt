package com.jydev.worksheet.application.worksheet.model

data class WorksheetModel(
    val worksheetId: Long,
    val name: String,
    val creatorId : Long,
    val problemIds : List<Long>,
)
