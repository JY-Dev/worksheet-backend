package com.jydev.worksheet.infra.worksheet

import com.jydev.worksheet.application.worksheet.WorksheetFinder
import com.jydev.worksheet.application.worksheet.model.StudentModel
import com.jydev.worksheet.application.worksheet.model.WorksheetAnalysisModel
import com.jydev.worksheet.domain.worksheet.QAssignedWorksheet
import com.jydev.worksheet.domain.worksheet.QWorksheetEvaluationResult
import com.jydev.worksheet.domain.worksheet.user.QStudent
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QueryDslWorksheetFinder(
    private val queryFactory: JPAQueryFactory
) : WorksheetFinder {

    override fun assignedStudents(worksheetId: Long): List<StudentModel> {
        val qAssignedWorksheet = QAssignedWorksheet.assignedWorksheet
        val qStudent = qAssignedWorksheet.learner

        val matchWorksheet = qAssignedWorksheet.worksheet.id.eq(worksheetId)

        return queryFactory.select(studentProjection(qStudent))
            .from(qAssignedWorksheet)
            .join(qAssignedWorksheet.learner)
            .where(matchWorksheet)
            .fetch()
    }

    private fun studentProjection(student: QStudent): Expression<StudentModel> {
        return Projections.constructor(
            StudentModel::class.java,
            student.id,
            student.name
        )
    }

    override fun searchAssignedWorksheetStudentIds(worksheetId: Long): List<Long> {
        val qAssignedWorksheet = QAssignedWorksheet.assignedWorksheet
        val learnerIdProjection = qAssignedWorksheet.learner.id

        val matchWorksheetId = qAssignedWorksheet.worksheet.id.eq(worksheetId)

        return queryFactory.select(learnerIdProjection)
            .from(qAssignedWorksheet)
            .where(matchWorksheetId)
            .fetch()
    }

    override fun searchWorksheetAnalysisDataList(worksheetId: Long): List<WorksheetAnalysisModel> {
        val qAssignedWorksheet = QAssignedWorksheet.assignedWorksheet
        val qEvaluationResult = qAssignedWorksheet.evaluationResult

        return queryFactory.select(worksheetAnalysisProjection(qAssignedWorksheet, qEvaluationResult))
            .from(qAssignedWorksheet)
            .where(qAssignedWorksheet.worksheet.id.eq(worksheetId))
            .fetch()
    }

    private fun worksheetAnalysisProjection(assignedWorksheet: QAssignedWorksheet, evaluationResult: QWorksheetEvaluationResult) : Expression<WorksheetAnalysisModel> {
        return Projections.constructor(
            WorksheetAnalysisModel::class.java,
            assignedWorksheet.learner.id,
            evaluationResult.problemEvaluations,
            evaluationResult.correctCount
        )
    }
}