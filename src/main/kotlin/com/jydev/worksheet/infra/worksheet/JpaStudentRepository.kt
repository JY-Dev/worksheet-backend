package com.jydev.worksheet.infra.worksheet

import com.jydev.worksheet.domain.worksheet.user.Student
import com.jydev.worksheet.domain.worksheet.user.StudentRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaStudentRepository : JpaRepository<Student, Long>, StudentRepository {
}