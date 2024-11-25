package com.example.goldpad.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.goldpad.database.converter.Converters
import com.example.goldpad.database.dao.RequestDao
import com.example.goldpad.database.dao.UserDao
import com.example.goldpad.database.dao.WaitingDao
import com.example.goldpad.database.entities.Request
import com.example.goldpad.database.entities.User
import com.example.goldpad.database.entities.Waiting

@Database(entities = [User::class, Request::class, Waiting::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GoldPadDatabase:RoomDatabase()
{
    abstract fun userDao(): UserDao
    abstract fun RequestDao(): RequestDao
    abstract fun WaitingDao(): WaitingDao
}