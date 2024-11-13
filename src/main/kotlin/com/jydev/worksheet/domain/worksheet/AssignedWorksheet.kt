package com.jydev.worksheet.domain.worksheet

import com.jydev.worksheet.core.jpa.TimeAuditableEntity
import com.jydev.worksheet.core.jpa.converter.SubmittedAnswerListConverter
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
        val problemOrderMap = createProblemOrderMap()
        val evaluationAnswers = evaluator.evaluate(submittedAnswers)

        this.submittedAnswers = submittedAnswers
        this.evaluationResult = WorksheetEvaluationResult.createResult(
            problemOrderMap = problemOrderMap,
            evaluatedAnswers = evaluationAnswers
        )
    }

    private fun createProblemOrderMap(): Map<Long, Int> {
        return worksheet.problemIds
            .mapIndexed { idx, problemId -> problemId to idx }
            .toMap()
    }

    fun isAlreadyEvaluated(): Boolean = evaluationResult.hasNoResult().not()
}
