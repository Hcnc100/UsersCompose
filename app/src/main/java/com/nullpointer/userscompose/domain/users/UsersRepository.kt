package com.nullpointer.userscompose.domain.users

import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    val listUsers: Flow<List<User>>
    suspend fun addNewUser():User
    suspend fun deleterUser(user:User)
    suspend fun deleterAllUsers()
    suspend fun deleterUserByIds(list: List<Long>)
    suspend fun getUserById(id:Long):User?
}