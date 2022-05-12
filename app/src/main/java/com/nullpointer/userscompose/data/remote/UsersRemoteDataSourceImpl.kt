package com.nullpointer.userscompose.data.remote

import com.nullpointer.userscompose.models.User

class UsersRemoteDataSourceImpl(
    private val usersApiServices: UserApiServices
):UsersRemoteDataSource {

    override suspend fun getNewUser():User{
        return User.fromUserResponse(usersApiServices.getUser().users[0])
    }

}