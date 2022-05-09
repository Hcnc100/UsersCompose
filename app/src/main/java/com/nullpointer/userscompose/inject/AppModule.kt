package com.nullpointer.userscompose.inject

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nullpointer.userscompose.data.local.room.UsersDao
import com.nullpointer.userscompose.data.local.room.UsersDatabase
import com.nullpointer.userscompose.data.remote.UserDataSource
import com.nullpointer.userscompose.domain.users.UserRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "BASE_URL"

    @Singleton
    @Provides
    @Named(BASE_URL)
    fun provideBaseUrl(): String =
        "https://randomuser.me/api/"

    @Provides
    @Singleton
    fun provideRetrofit(@Named(BASE_URL) baseUrl: String): Retrofit =
        Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create()
        ).baseUrl(baseUrl).build()

    @Provides
    @Singleton
    fun provideUserDataSource(retrofit: Retrofit): UserDataSource =
        retrofit.create(UserDataSource::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): UsersDatabase = Room.databaseBuilder(
        context,
        UsersDatabase::class.java,
        "USERS_DATABASE"
    ).build()

    @Provides
    @Singleton
    fun provideUsersDao(
        database: UsersDatabase,
    ): UsersDao = database.getUsersDao()


}