package com.example.goldpad.di

import android.content.Context
import androidx.room.Room
import com.example.goldpad.database.GoldPadDatabase
import com.example.goldpad.database.dao.RequestDao
import com.example.goldpad.database.dao.UserDao
import com.example.goldpad.database.dao.WaitingDao
import com.example.goldpad.utils.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): GoldPadDatabase {
        return Room.databaseBuilder(
            appContext,
            GoldPadDatabase::class.java,
            "gold_pad_db"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDao(appDatabase: GoldPadDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideRequestDao(appDatabase: GoldPadDatabase): RequestDao {
        return appDatabase.RequestDao()
    }

    @Provides
    fun provideWaitingDao(appDatabase: GoldPadDatabase): WaitingDao {
        return appDatabase.WaitingDao()
    }

    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}
