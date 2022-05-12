package com.nullpointer.userscompose.data.remote

class UsersRemoteDataSource(
    private val usersApiServices: UserApiServices
) {
    suspend fun getNewUser()=
        usersApiServices.getUser()
}