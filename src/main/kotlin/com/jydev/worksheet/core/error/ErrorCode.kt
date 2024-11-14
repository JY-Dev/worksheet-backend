package com.jydev.worksheet.core.error

enum class ErrorCode(val description: String) {
    DEFAULT("미정"),

    // A01(학습지)
    A01001("이미 존재하는 학습지명입니다."),
    A01002("유효하지 않은 문제가 포함되어 있습니다."),
    A01003("이미 채점이 완료된 학습지입니다."),

    // A05(공통)
    A05001("요청한 데이터가 요구사항을 충족하지 않습니다.")
    ;

}