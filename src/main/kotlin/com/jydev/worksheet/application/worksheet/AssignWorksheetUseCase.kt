package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.worksheet.error.WorksheetAssignPermissionException
import com.jydev.worksheet.domain.worksheet.AssignedWorksheet
import com.jydev.worksheet.domain.worksheet.AssignedWorksheetRepository
import com.jydev.worksheet.domain.worksheet.WorksheetRepository
import com.jydev.worksheet.domain.worksheet.user.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class AssignWorksheetUseCase(
    private val transactionTemplate: TransactionTemplate,
    private val assignedWorksheetRepository: AssignedWorksheetRepository,
    private val worksheetRepository: WorksheetRepository,
    private val studentRepository: StudentRepository,
    private val worksheetFinder: WorksheetFinder,
) {

    operator fun invoke(
        teacherId: Long,
        worksheetId: Long,
        studentIds: List<Long>
    ) : List<Long> {
        val worksheet = worksheetRepository.findById(worksheetId)
            ?: throw NoSuchElementException("Worksheet with id $worksheetId does not exist.")

        val matchWorksheetCreator = worksheet.creator.id == teacherId
        if (matchWorksheetCreator.not()) {
            throw WorksheetAssignPermissionException("Not matching Worksheet with id $teacherId.")
        }

        val assignedWorksheets = mutableListOf<Long>().apply {
            val alreadyAssignedStudentIds = worksheetFinder.searchAssignedWorksheetStudentIds(worksheetId).toSet()
            this.addAll(studentIds.filter { alreadyAssignedStudentIds.contains(it).not() })
        }.mapNotNull {
            studentRepository.findById(it)
        }.map { student ->
            AssignedWorksheet(
                worksheet = worksheet,
                learner = student
            )
        }

        val assignedStudentIds = transactionTemplate.execute {
            assignedWorksheetRepository.saveAll(assignedWorksheets).map {
                it.learner.id!!
            }
        } ?: emptyList()

        return assignedStudentIds
    }
}