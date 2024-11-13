package com.jydev.worksheet.infra.worksheet

import com.jydev.worksheet.application.worksheet.WorksheetFinder
import com.jydev.worksheet.domain.worksheet.QAssignedWorksheet
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QueryDslWorksheetFinder(
    private val queryFactory : JPAQueryFactory
) : WorksheetFinder {

    override fun searchAssignedWorksheetStudentIds(worksheetId: Long): List<Long> {
        val qAssignedWorksheet = QAssignedWorksheet.assignedWorksheet
        val learnerIdProjection = qAssignedWorksheet.learner.id

        val matchWorksheetId = qAssignedWorksheet.worksheet.id.eq(worksheetId)

        return queryFactory.select(learnerIdProjection)
            .from(qAssignedWorksheet)
            .where(matchWorksheetId)
            .fetch()
    }
}