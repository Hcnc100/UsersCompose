package com.nullpointer.userscompose.domain.users

import com.nullpointer.userscompose.core.utils.InternetCheck
import com.nullpointer.userscompose.core.utils.InternetCheckError
import com.nullpointer.userscompose.core.utils.ServerTimeOut
import com.nullpointer.userscompose.data.local.room.UsersDao
import com.nullpointer.userscompose.data.remote.UserDataSource
import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val usersDao: UsersDao,
    private val userDataSource: UserDataSource,
) : UsersRepository {

    override val listUsers: Flow<List<User>> = usersDao.getAllUsers()

    override suspend fun addNewUser(): User {
        // * if the internet is not available throw exception
        if (!InternetCheck.isNetworkAvailable()) throw InternetCheckError()
        val newUser = withTimeoutOrNull(5_000) {
            val userResponse = userDataSource.getUser().users[0]
            User.fromUserResponse(userResponse)
        }
        // * if time out so send time out error

        if (newUser != null) {
            usersDao.addNewUser(newUser)
            return newUser
        }else{
            throw ServerTimeOut()
        }
    }

    override suspend fun deleterUser(user: User) =
        usersDao.deleterUser(user)

    override suspend fun deleterAllUsers() =
        usersDao.deleterAllUsers()

    override suspend fun deleterUserByIds(list: List<Long>) =
        usersDao.deleterUsersBYId(list)

    override suspend fun getUserById(id: Long): User? =
        usersDao.getUserById(id)
}