package com.jydev.worksheet.core.jpa

import com.jydev.worksheet.core.jpa.converter.InstantToUtcConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class TimeAuditableEntity(

) {
    @CreatedDate
    @Column(name = "CT_UTC")
    @Convert(converter = InstantToUtcConverter::class)
    var creationTime: Instant = Instant.now()
        protected set

    @LastModifiedDate
    @Column(name = "UT_UTC")
    @Convert(converter = InstantToUtcConverter::class)
    var updateTime: Instant = Instant.now()
        protected set
}

