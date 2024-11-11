package com.jydev.worksheet.domain.problem

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UnitCodeTest {

    @Test
    fun `정상적인 유닛 코드 생성`() {
        val unitCode = UnitCode("${UnitCode.PREFIX}1234")
        assertEquals("${UnitCode.PREFIX}1234", unitCode.value())
    }

    @Test
    fun `잘못된 유닛 코드 형식으로 생성 시 예외 발생`() {
        assertThrows<IllegalArgumentException> {
            UnitCode("invalid1234")
        }
    }

    @Test
    fun `숫자만 포함된 유닛 코드 생성`() {
        val unitCode = UnitCode("${UnitCode.PREFIX}1")
        assertEquals("uc1", unitCode.value())
    }
}