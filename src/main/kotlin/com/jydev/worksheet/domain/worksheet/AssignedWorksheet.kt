package com.jydev.worksheet.domain.worksheet

import com.jydev.worksheet.core.jpa.TimeAuditableEntity
import com.jydev.worksheet.domain.worksheet.evaluation.SubmittedAnswerListConverter
import com.jydev.worksheet.core.util.toIndexMap
import com.jydev.worksheet.domain.worksheet.evaluation.AnswerEvaluator
import com.jydev.worksheet.domain.worksheet.evaluation.SubmittedAnswer
import com.jydev.worksheet.domain.worksheet.user.Student
import jakarta.persistence.*
import java.time.Instant

@Table(name = "ASSIGNED_WORKSHEET")
@Entity
class AssignedWorksheet(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKSHEET_ID")
    val worksheet: Worksheet,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    val learner: Student,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
) : TimeAuditableEntity() {
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "problemEvaluations", column = Column(name = "PROBLEM_EVALUATIONS")),
        AttributeOverride(name = "correctCount", column = Column(name = "CORRECT_CNT"))
    )
    var evaluationResult: WorksheetEvaluationResult = WorksheetEvaluationResult.createNoResult()
        protected set

    @Column(name = "SUBMITTED_ANSWERS")
    @Convert(converter = SubmittedAnswerListConverter::class)
    var submittedAnswers: List<SubmittedAnswer> = emptyList()
        protected set

    val assignedTime: Instant
        get() = creationTime

    fun evaluate(evaluator: AnswerEvaluator, submittedAnswers: List<SubmittedAnswer>) {
        validateSubmittedAnswers(submittedAnswers)

        val problemIdIndexMap = worksheet.problemIds.toIndexMap()
        val evaluationAnswers = evaluator.evaluate(submittedAnswers)
            .sortedBy { problemIdIndexMap[it.problemId] }

        this.submittedAnswers = submittedAnswers
        this.evaluationResult = WorksheetEvaluationResult.createResult(evaluationAnswers)
    }

    private fun validateSubmittedAnswers(submittedAnswers: List<SubmittedAnswer>) {
        val problemIds = worksheet.problemIds
        val problemSize = problemIds.size

        val matchAnswerSize = problemSize == submittedAnswers.size
        if (matchAnswerSize.not()) {
            throw IllegalArgumentException(
                "Submitted answers size ${submittedAnswers.size} does not match the expected size in the worksheet."
            )
        }

        worksheet.problemIds.forEachIndexed { idx, problemId ->
            val matchProblem = submittedAnswers[idx].problemId == problemId
            if (matchProblem.not()) {
                throw IllegalArgumentException(
                    "Problem ID : $problemId does not match in your submit answer"
                )
            }
        }
    }

    fun isAlreadyEvaluated(): Boolean = evaluationResult.hasNoResult().not()
}
