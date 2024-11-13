package com.jydev.worksheet.domain.worksheet

import com.jydev.worksheet.core.jpa.TimeAuditableEntity
import com.jydev.worksheet.core.jpa.converter.LongListConverter
import com.jydev.worksheet.domain.worksheet.user.Teacher
import jakarta.persistence.*

@Table(name = "WORKSHEET")
@Entity
class Worksheet(

    @Column(name = "NAME")
    val name : String,

    @Column(name = "PROBLEM_IDS")
    @Convert(converter = LongListConverter::class)
    val problemIds : List<Long>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEACHER_ID")
    val creator : Teacher,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
) : TimeAuditableEntity() {

}
