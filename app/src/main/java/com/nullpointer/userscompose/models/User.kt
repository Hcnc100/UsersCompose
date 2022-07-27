package com.nullpointer.userscompose.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users_table")
@Parcelize
data class User(
    val name: String,
    val lastName: String,
    val city: String,
    val imgUser: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    var isSelect: Boolean = false

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (name != other.name) return false
        if (lastName != other.lastName) return false
        if (city != other.city) return false
        if (imgUser != other.imgUser) return false
        if (id != other.id) return false
        if (isSelect != other.isSelect) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + imgUser.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + isSelect.hashCode()
        return result
    }


}
