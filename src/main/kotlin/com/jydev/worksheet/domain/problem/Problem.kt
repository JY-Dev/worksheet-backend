package com.jydev.worksheet.domain.problem

import com.jydev.worksheet.core.jpa.TimeAuditableEntity
import jakarta.persistence.*


@Table(name = "PROBLEM")
@Entity
class Problem(

    @Embedded
    @AttributeOverride(name = "numericCode", column = Column(name = "UNIT_CODE"))
    val unitCode: UnitCode,

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    val type: ProblemType,

    @Column(name = "DIFFICULTY", columnDefinition = "TINYINT")
    private val difficulty : Int,

    @Column(name = "ANSWER")
    private val answer: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
) : TimeAuditableEntity() {

}