package com.nullpointer.userscompose.usersRepo.repository


import com.nullpointer.userscompose.core.utils.ServerTimeOut
import com.nullpointer.userscompose.data.local.datasource.UsersLocalDataSource
import com.nullpointer.userscompose.data.remote.UsersRemoteDataSource
import com.nullpointer.userscompose.domain.users.UsersRepository
import com.nullpointer.userscompose.models.User
import com.nullpointer.userscompose.usersRepo.data.local.MockUserDao
import com.nullpointer.userscompose.usersRepo.data.local.MockUserLocalDataSource
import com.nullpointer.userscompose.usersRepo.data.remote.MockUserRemoteDataSource
import com.nullpointer.userscompose.usersRepo.data.remote.UsersFakeServices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class MockUserRepository(
    private val remoteDataSource: UsersRemoteDataSource ,
    private val localDataSource: UsersLocalDataSource ,
) : UsersRepository {

    override val listUsers: Flow<List<User>> =
        localDataSource.listUsers

    override suspend fun addNewUser(): User {
        val newUser= withTimeoutOrNull(5_000){
            remoteDataSource.getNewUser()
        } ?:throw  ServerTimeOut()
        localDataSource.addNewUser(newUser)
        return newUser
    }

    override suspend fun deleterUser(user: User) {
        localDataSource.deleterUser(user)
    }

    override suspend fun deleterAllUsers() {
        localDataSource.deleterAllUsers()
    }

    override suspend fun deleterUserByIds(list: List<Long>) {
        localDataSource.deleterUserById(list)
    }

    override suspend fun getUserById(id: Long): User? {
        return localDataSource.getUserById(id)
    }
}

