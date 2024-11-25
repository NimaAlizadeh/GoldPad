package com.example.goldpad.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.goldpad.database.converter.Converters

@Entity(tableName = "waiting")
@TypeConverters(Converters::class)
data class Waiting(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var requestId: Int = 0,
    var requests: List<Request> = emptyList(),
    var userId: Int = 0
)
