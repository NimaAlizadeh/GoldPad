package com.example.goldpad.database.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.example.goldpad.database.entities.Request
import com.example.goldpad.database.entities.User

data class RequestWithUser(
    @Embedded val request: Request,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: User
)
