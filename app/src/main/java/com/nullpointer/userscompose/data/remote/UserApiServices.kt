package com.nullpointer.userscompose.data.remote

import com.nullpointer.userscompose.models.ApiResponse
import retrofit2.http.GET

interface UserApiServices {
    companion object{
        const val USER_END_POINT="?inc=name,location,email,picture&nat=es"
    }
    // * entry point
    @GET(USER_END_POINT)
    suspend fun getUser(): ApiResponse
}