package com.nullpointer.userscompose.usersRepo.data.local

import com.nullpointer.userscompose.data.local.datasource.UsersLocalDataSource
import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.flow.Flow

class MockUserLocalDataSource(
    private val mockUserDao: MockUserDao
):UsersLocalDataSource {

    override val listUsers: Flow<List<User>> =
        mockUserDao.getAllUsers()

    override suspend fun addNewUser(user: User) =
        mockUserDao.addNewUser(user)

    override suspend fun addListUsers(list: List<User>) =
        mockUserDao.addListUsers(list)

    override suspend fun deleterUser(user: User) =
        mockUserDao.deleterUser(user)

    override suspend fun deleterUserById(listIds: List<Long>) =
        mockUserDao.deleterUsersById(listIds)

    override suspend fun deleterAllUsers() =
        mockUserDao.deleterAllUsers()

    override suspend fun getUserById(id: Long): User? =
        mockUserDao.getUserById(id)
}