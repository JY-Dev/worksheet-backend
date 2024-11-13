package com.jydev.worksheet.domain.worksheet.user

import com.jydev.worksheet.core.jpa.TimeAuditableEntity
import jakarta.persistence.*

@Table(name = "TEACHER")
@Entity
class Teacher(
    @Column(name = "NAME")
    val name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long? = null
) : TimeAuditableEntity() {
}