package com.nullpointer.userscompose.models

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users_table")
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val city: String = "",
    val lastName: String = "",
    val imgUser: String = "",
    val timestamp: Long = System.currentTimeMillis(),
) : Parcelable {

    @IgnoredOnParcel
    @delegate:Ignore
    var isSelect: Boolean by mutableStateOf(false)

    companion object {
        fun fromUserResponse(userResponse: ApiResponse.UserResponse): User {
            return User(
                name = userResponse.name.first,
                lastName = userResponse.name.last,
                city = userResponse.location.city,
                imgUser = userResponse.picture.large,
            )
        }

        fun getUserRandom(): User {
            val newId=(0..100).random().toLong()
            return User(
                id = newId,
                name = "Pepe $newId",
                lastName = "Basurita $newId",
                city = "Venezuela",
                imgUser = "https://picsum.photos/200/300",
            )
        }

        fun generateListUsers(numberUsers:Long): List<User> {
            return (1..numberUsers).map {
                 User(
                    id = it,
                    name = "Pepe $it",
                    lastName = "Basurita $it",
                    city = "Venezuela",
                    imgUser = "https://picsum.photos/200/300",
                )
            }
        }
    }
}
