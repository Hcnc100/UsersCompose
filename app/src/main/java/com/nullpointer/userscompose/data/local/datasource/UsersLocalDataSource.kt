package com.nullpointer.userscompose.data.local.datasource

import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.flow.Flow

interface UsersLocalDataSource {
    val listUsers: Flow<List<User>>
    suspend fun addNewUser(user: User)
    suspend fun addListUsers(list: List<User>)
    suspend fun deleterUser(user: User)
    suspend fun deleterUserById(listIds: List<Long>)
    suspend fun deleterAllUsers()
    suspend fun getUserById(id: Long):User?
}