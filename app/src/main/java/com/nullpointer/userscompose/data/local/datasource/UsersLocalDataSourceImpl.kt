package com.nullpointer.userscompose.data.local.datasource

import com.nullpointer.userscompose.data.local.room.UsersDao
import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.flow.Flow

class UsersLocalDataSourceImpl(
    private val usersDao: UsersDao,
):UsersLocalDataSource{
    override val listUsers:Flow<List<User>> = usersDao.getAllUsers()

    override suspend fun addNewUser(user: User) =
        usersDao.addNewUser(user)

    override suspend fun addListUsers(list: List<User>) =
        usersDao.addListUsers(list)

    override suspend fun deleterUser(user: User) =
        usersDao.deleterUser(user)

    override suspend fun deleterUserById(listIds: List<Long>) =
        usersDao.deleterUsersById(listIds)

    override suspend fun deleterAllUsers() =
        usersDao.deleterAllUsers()

    override suspend fun getUserById(id: Long) =
        usersDao.getUserById(id)
}