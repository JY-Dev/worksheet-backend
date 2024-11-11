package com.jydev.worksheet.domain.problem

import jakarta.persistence.Embeddable

@Embeddable
class UnitCode(unitCode: String) {

    private var numericCode: Int = 0

    init {
        val numericPart = UNIT_CODE_REGEX.find(unitCode)?.value
            ?: throw IllegalArgumentException("Invalid unit code format")
        this.numericCode = numericPart.toInt()
    }

    fun value(): String = PREFIX + numericCode

    companion object {
        const val PREFIX = "uc"
        private val UNIT_CODE_REGEX = "(?<=^uc)\\d+$".toRegex()
    }
}