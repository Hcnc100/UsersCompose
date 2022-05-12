package com.nullpointer.userscompose.domain.users

import com.nullpointer.userscompose.core.utils.InternetCheck
import com.nullpointer.userscompose.core.utils.InternetCheckError
import com.nullpointer.userscompose.core.utils.ServerTimeOut
import com.nullpointer.userscompose.data.local.room.UsersLocalDataSource
import com.nullpointer.userscompose.data.remote.UsersRemoteDataSource
import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val usersLocalDataSource: UsersLocalDataSource,
    private val usersRemoteDataSource: UsersRemoteDataSource
) : UsersRepository {

    override val listUsers: Flow<List<User>> = usersLocalDataSource.listUsers

    override suspend fun addNewUser(): User {
        // * if the internet is not available throw exception
        if (!InternetCheck.isNetworkAvailable()) throw InternetCheckError()
        val newUser = withTimeoutOrNull(5_000) {
            val userResponse = usersRemoteDataSource.getNewUser().users[0]
            User.fromUserResponse(userResponse)
        }
        // * if time out so send time out error

        if (newUser != null) {
            usersLocalDataSource.addNewUser(newUser)
            return newUser
        }else{
            throw ServerTimeOut()
        }
    }

    override suspend fun deleterUser(user: User) =
        usersLocalDataSource.deleterUser(user)

    override suspend fun deleterAllUsers() =
        usersLocalDataSource.deleterAllUsers()

    override suspend fun deleterUserByIds(list: List<Long>) =
        usersLocalDataSource.deleterUserById(list)

    override suspend fun getUserById(id: Long): User? =
        usersLocalDataSource.getUserById(id)
}