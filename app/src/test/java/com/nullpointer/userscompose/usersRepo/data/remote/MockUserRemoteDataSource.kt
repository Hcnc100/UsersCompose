package com.nullpointer.userscompose.usersRepo.data.remote

import com.nullpointer.userscompose.data.remote.UserApiServices
import com.nullpointer.userscompose.data.remote.UsersRemoteDataSource
import com.nullpointer.userscompose.models.User

class MockUserRemoteDataSource(
    private val usersApiServices: UserApiServices,
) : UsersRemoteDataSource {
    override suspend fun getNewUser(): User {
        return User.fromUserResponse(usersApiServices.getUser().users[0])
    }
}