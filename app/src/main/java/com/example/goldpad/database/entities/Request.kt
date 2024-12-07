package com.example.goldpad.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "request",
    foreignKeys = [
        ForeignKey(
            entity = User::class, // Existing relationship with User
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Request(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var userId: Int = 0,    // Existing foreign key to User's ID
    var requestId: Int = 0, // Foreign key to Waiting's ID
    var requestText: String = "",
    var amount: Int = 0,
    var mode: Boolean = false, // true: buyer, false: seller
    var proposedValue: Double? = null,
    var isActive: Boolean = true
)
