package com.nullpointer.userscompose.domain.users

import android.accounts.NetworkErrorException
import com.nullpointer.userscompose.core.utils.InternetCheck
import com.nullpointer.userscompose.data.local.datasource.UsersLocalDataSourceImpl
import com.nullpointer.userscompose.data.remote.UsersRemoteDataSourceImpl
import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val usersLocalDataSource: UsersLocalDataSourceImpl,
    private val usersRemoteDataSource: UsersRemoteDataSourceImpl
) : UsersRepository {

    override val listUsers: Flow<List<User>> = usersLocalDataSource.listUsers

    override suspend fun addNewUser(): User {
        // * if the internet is not available throw exception
        if (!InternetCheck.isNetworkAvailable()) throw NetworkErrorException()
        val newUser = withTimeoutOrNull(5_000) {
            usersRemoteDataSource.getNewUser()
        }?.also {
            usersLocalDataSource.addNewUser(it)
        }
        // * if time out so send time out error
        return newUser!!
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