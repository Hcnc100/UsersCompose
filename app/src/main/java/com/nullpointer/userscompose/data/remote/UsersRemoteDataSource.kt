package com.nullpointer.userscompose.data.remote

import com.nullpointer.userscompose.models.User

interface UsersRemoteDataSource {
    suspend fun getNewUser(): User
}