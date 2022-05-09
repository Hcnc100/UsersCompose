package com.nullpointer.userscompose.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nullpointer.userscompose.models.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class UsersDatabase:RoomDatabase() {
    abstract fun getUsersDao():UsersDao
}