package com.jydev.worksheet.domain.worksheet.user

interface TeacherRepository {
    fun findById(id: Long) : Teacher?
    fun save(teacher: Teacher): Teacher
    fun delete(teacher: Teacher)
}