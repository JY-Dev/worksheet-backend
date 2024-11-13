package com.jydev.worksheet.infra.worksheet

import com.jydev.worksheet.domain.worksheet.Worksheet
import com.jydev.worksheet.domain.worksheet.WorksheetRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaWorksheetRepository : JpaRepository<Worksheet, Long>, WorksheetRepository {
}