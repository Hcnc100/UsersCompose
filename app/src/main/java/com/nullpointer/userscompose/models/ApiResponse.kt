package com.nullpointer.userscompose.models

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    // ! this for call for other name to class
    // ! result -> UserResponse
    @SerializedName("results")
    val users: List<UserResponse>
) {
    data class UserResponse(
        val email: String,
        val location: Location,
        val name: Name,
        val picture: Picture,
        val id:Int=0
    ) {
        data class Location(
            val city: String,
            val country: String,
            val postcode: Int,
            val state: String,
            val street: Street
        ) {
            data class Street(
                val name: String,
                val number: Int
            )
        }

        data class Name(
            val first: String,
            val last: String,
            val title: String
        )

        data class Picture(
            val large: String
        )
    }
}