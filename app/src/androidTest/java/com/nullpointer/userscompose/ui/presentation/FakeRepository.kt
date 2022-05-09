package com.nullpointer.userscompose.ui.presentation

import com.nullpointer.userscompose.domain.users.UsersRepository
import com.nullpointer.userscompose.inject.RepositoryModule
import com.nullpointer.userscompose.models.User
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
class FakeRepositoryModule {

    @Provides
    @Singleton
    fun userRepository(): UsersRepository =
        object : UsersRepository {

            private val users = MutableStateFlow<List<User>>(listOf())
            override val listUsers: Flow<List<User>> = users

            override suspend fun deleterUser(user: User) {
                users.value = users.value.toMutableList().apply { remove(user) }
            }

            override suspend fun deleterAllUsers() {

            }

            override suspend fun deleterUserByIds(list: List<Long>) {

            }

            override suspend fun getUserById(id: Long): User? {
              return null
            }



            override suspend fun addNewUser(): User {
                val userList = users.value
                val newUser = User(
                    "Name ${userList.size}",
                    "LastName ${userList.size}",
                    "City",
                    "Image",
                )
                users.value = users.value.toMutableList().apply { add(newUser) }
                return newUser
            }
        }
}