package com.jydev.worksheet.domain.worksheet.user

interface StudentRepository {
    fun findById(id: Long) : Student?
    fun save(student: Student): Student
    fun delete(student: Student)
}