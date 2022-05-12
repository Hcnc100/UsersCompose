package com.nullpointer.userscompose.inject

import com.nullpointer.userscompose.data.remote.UserApiServices
import com.nullpointer.userscompose.data.remote.UsersRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

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
    fun provideUserDataSource(retrofit: Retrofit): UserApiServices =
        retrofit.create(UserApiServices::class.java)

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(
        usersApiServices: UserApiServices
    ):UsersRemoteDataSource= UsersRemoteDataSource(usersApiServices)
}