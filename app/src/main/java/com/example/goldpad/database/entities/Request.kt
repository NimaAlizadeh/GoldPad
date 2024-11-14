package com.example.goldpad.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request")
data class Request (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var fromUser: Int = 0,
    var requestText: String = "",
    var amount: Int = 0,
    var mode: Boolean = false,
)