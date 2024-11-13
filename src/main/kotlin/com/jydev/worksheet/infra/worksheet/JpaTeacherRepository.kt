package com.jydev.worksheet.infra.worksheet

import com.jydev.worksheet.domain.worksheet.user.Teacher
import com.jydev.worksheet.domain.worksheet.user.TeacherRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaTeacherRepository : JpaRepository<Teacher, Long>, TeacherRepository {
}