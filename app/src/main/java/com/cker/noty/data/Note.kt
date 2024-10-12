package com.cker.noty.data

import java.util.UUID

data class Note(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
