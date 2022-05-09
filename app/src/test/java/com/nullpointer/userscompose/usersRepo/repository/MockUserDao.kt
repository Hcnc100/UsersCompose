package com.nullpointer.userscompose.usersRepo.repository

import com.nullpointer.userscompose.data.local.room.UsersDao
import com.nullpointer.userscompose.models.User
import com.nullpointer.userscompose.usersRepo.utils.listFakeUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MockUserDao : UsersDao {

    private val users = MutableStateFlow(listFakeUser)

    override fun getAllUsers(): Flow<List<User>> =
        users

    override fun addNewUser(user: User) {
        users.value = users.value.toMutableList().apply { add(user) }
    }

    override fun deleterUser(user: User) {
        users.value = users.value.toMutableList().apply { remove(user) }
    }

    override fun deleterUsersBYId(list: List<Long>) {
        val listUsersDeleter=users.value.filter {
            list.contains(it.id)
        }
        users.value = users.value.toMutableList().apply { removeAll(listUsersDeleter) }
    }

    override fun deleterAllUsers() {
        users.value= emptyList()
    }

    override fun getUserById(id: Long): User? {
        return users.value.firstOrNull { it.id == id }
    }
}