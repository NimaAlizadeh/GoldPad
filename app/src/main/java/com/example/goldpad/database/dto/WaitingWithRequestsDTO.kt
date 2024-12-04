package com.example.goldpad.database.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.example.goldpad.database.entities.Request
import com.example.goldpad.database.entities.User
import com.example.goldpad.database.entities.Waiting

data class WaitingWithRequestsDTO(
    @Embedded val waiting: Waiting,
    @Relation(
        parentColumn = "id", // Waiting's primary key
        entityColumn = "requestId" // Request's foreign key linking to Waiting
    )
    val requests: List<Request>,
    @Relation(
        parentColumn = "userId", // Waiting's foreign key linking to User
        entityColumn = "id" // User's primary key
    )
    val user: User
)
