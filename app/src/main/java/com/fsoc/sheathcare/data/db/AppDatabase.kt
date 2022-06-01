package com.fsoc.sheathcare.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fsoc.sheathcare.data.db.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object{
        const val DATABASE_NAME = "mydatabase"
    }
    abstract fun userDao(): UserDao
}