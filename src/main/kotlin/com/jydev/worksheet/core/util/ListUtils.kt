package com.jydev.worksheet.core.util

fun <T> List<T>.toIndexMap(): Map<T, Int> {
    return this.withIndex().associate { (index, value) -> value to index }
}