package com.nullpointer.userscompose.data.remote

import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.delay

class UsersRemoteDataSourceImpl(
    private val usersApiServices: UserApiServices
):UsersRemoteDataSource {

    override suspend fun getNewUser():User{
        delay(1500)
        return User.fromUserResponse(usersApiServices.getUser().users[0])
    }

}