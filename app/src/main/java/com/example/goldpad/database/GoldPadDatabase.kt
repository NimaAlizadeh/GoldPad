package com.example.goldpad.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.goldpad.database.dao.UserDao
import com.example.goldpad.database.entities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class GoldPadDatabase:RoomDatabase()
{
    abstract fun userDao(): UserDao
}