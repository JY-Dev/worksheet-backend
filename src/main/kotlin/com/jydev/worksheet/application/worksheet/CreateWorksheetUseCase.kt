package com.jydev.worksheet.application.worksheet

import com.jydev.worksheet.application.problem.ProblemFinder
import com.jydev.worksheet.application.worksheet.model.WorksheetModel
import com.jydev.worksheet.domain.worksheet.Worksheet
import com.jydev.worksheet.domain.worksheet.WorksheetRepository
import com.jydev.worksheet.domain.worksheet.user.TeacherRepository
import com.jydev.worksheet.application.worksheet.error.DuplicateWorksheetNameException
import com.jydev.worksheet.application.worksheet.error.InvalidProblemContainException
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class CreateWorksheetUseCase(
    private val transactionTemplate: TransactionTemplate,
    private val problemFinder: ProblemFinder,
    private val worksheetRepository: WorksheetRepository,
    private val teacherRepository: TeacherRepository
) {

    operator fun invoke(
        teacherId : Long,
        problemIds : List<Long>,
        worksheetName: String,
    ) : WorksheetModel {
        val teacher = teacherRepository.findById(teacherId)
            ?: throw NoSuchElementException("Teacher with id: $teacherId does not exist")

        val duplicateWorksheetName = worksheetRepository.existsByCreatorIdAndName(
            creatorId = teacherId,
            name = worksheetName
        )
        if(duplicateWorksheetName) {
            throw DuplicateWorksheetNameException("Duplicate worksheet name: $worksheetName")
        }

        val existProblemCount = problemFinder.countExistingProblems(problemIds)
        val containInvalidProblems = problemIds.size != existProblemCount
        if (containInvalidProblems) {
            throw InvalidProblemContainException("Invalid problems contain $problemIds")
        }

        val worksheet = Worksheet(
            name = worksheetName,
            problemIds = problemIds,
            creator = teacher
        )

        val model = transactionTemplate.execute {
            val savedWorksheet = worksheetRepository.save(worksheet)
            WorksheetModel(
                worksheetId = worksheet.id!!,
                name = savedWorksheet.name,
                creatorId = savedWorksheet.creator.id!!,
                problemIds = savedWorksheet.problemIds,
            )
        } ?: throw IllegalStateException("Failed to create model")

        return model
    }
}