package com.nullpointer.userscompose.usersRepo.data.local

import com.nullpointer.userscompose.data.local.room.UsersDao
import com.nullpointer.userscompose.models.User
import com.nullpointer.userscompose.usersRepo.utils.listFakeUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockUserDao : UsersDao {

    private val users = MutableStateFlow(listFakeUser)

    override fun getAllUsers(): Flow<List<User>> =
        users

    override suspend fun addListUsers(list: List<User>) {
        users.value = users.value.toMutableList().apply { addAll(list) }
    }

    override suspend fun deleterUsersById(list: List<Long>) {
        val listUsersDeleter=users.value.filter {
            list.contains(it.id)
        }
        users.value = users.value.toMutableList().apply { removeAll(listUsersDeleter) }
    }

    override suspend fun addNewUser(user: User) {
        users.value = users.value.toMutableList().apply { add(user) }
    }

    override suspend fun deleterUser(user: User) {
        users.value = users.value.toMutableList().apply { remove(user) }
    }


    override suspend fun deleterAllUsers() {
        users.value= emptyList()
    }

    override suspend fun getUserById(id: Long): User? {
        return users.value.firstOrNull { it.id == id }
    }
}