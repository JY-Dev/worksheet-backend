package com.jydev.worksheet.infra.worksheet

import com.jydev.worksheet.domain.worksheet.AssignedWorksheet
import com.jydev.worksheet.domain.worksheet.AssignedWorksheetRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaAssignedWorksheetRepository : JpaRepository<AssignedWorksheet, Long>, AssignedWorksheetRepository {
}