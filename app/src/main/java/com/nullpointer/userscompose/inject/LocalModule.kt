package com.nullpointer.userscompose.inject

import android.content.Context
import androidx.room.Room
import com.nullpointer.userscompose.data.local.datasource.UsersLocalDataSource
import com.nullpointer.userscompose.data.local.room.UsersDao
import com.nullpointer.userscompose.data.local.room.UsersDatabase
import com.nullpointer.userscompose.data.local.datasource.UsersLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    private const val NAME_DATABASE="USERS_DATABASE"

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): UsersDatabase = Room.databaseBuilder(
        context,
        UsersDatabase::class.java,
        NAME_DATABASE
    ).build()

    @Provides
    @Singleton
    fun provideUsersDao(
        database: UsersDatabase,
    ): UsersDao = database.getUsersDao()


    @Provides
    @Singleton
    fun provideUsersLocalUsersDataSource(
        usersDao: UsersDao
    ): UsersLocalDataSourceImpl = UsersLocalDataSourceImpl(usersDao)

}