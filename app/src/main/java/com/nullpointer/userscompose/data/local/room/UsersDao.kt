package com.nullpointer.userscompose.data.local.room

import androidx.room.*
import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {

    @Query("SELECT * FROM users_table ORDER BY timestamp DESC")
    fun getAllUsers():Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewUser(user:User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addListUsers(list: List<User>)

    @Delete
    suspend fun deleterUser(user: User)

    @Query("DELETE FROM users_table WHERE id IN (:list)")
    suspend fun deleterUsersById(list:List<Long>)

    @Query("DELETE FROM users_table")
    suspend fun deleterAllUsers()

    @Query("SELECT * FROM users_table WHERE id=:id")
    suspend fun getUserById(id:Long):User?
}