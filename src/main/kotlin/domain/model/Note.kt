package ru.normno.domain.model

import java.time.Instant

data class Note(
    val id: Long = 0,
    val text: String,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
)
