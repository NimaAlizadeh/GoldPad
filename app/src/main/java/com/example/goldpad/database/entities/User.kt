package com.example.goldpad.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    var token: String? = "",
    val username: String? = "",
    val shebaCode: String? = "", // shomare sheba
    val bankName: String? = "",
    val userId: String? = "", // code melli
    val isAdmin: Boolean = false
)