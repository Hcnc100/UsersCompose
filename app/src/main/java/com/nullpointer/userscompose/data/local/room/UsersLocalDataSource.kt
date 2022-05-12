package com.nullpointer.userscompose.data.local.room

import com.nullpointer.userscompose.models.User

class UsersLocalDataSource(
    private val usersDao: UsersDao,
) {
    val listUsers = usersDao.getAllUsers()

    suspend fun addNewUser(user: User) =
        usersDao.addNewUser(user)

    suspend fun addListUsers(list: List<User>) =
        usersDao.addListUsers(list)

    suspend fun deleterUser(user: User) =
        usersDao.deleterUser(user)

    suspend fun deleterUserById(listIds: List<Long>) =
        usersDao.deleterUsersById(listIds)

    suspend fun deleterAllUsers() =
        usersDao.deleterAllUsers()

    suspend fun getUserById(id: Long) =
        usersDao.getUserById(id)
}