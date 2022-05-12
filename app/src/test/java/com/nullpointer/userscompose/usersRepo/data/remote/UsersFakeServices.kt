package com.nullpointer.userscompose.usersRepo.data.remote

import com.nullpointer.userscompose.data.remote.UserApiServices
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class UsersFakeServices {
    companion object {


        val successDispatchers: Dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                print(request.path)
                return when (request.path) {
                    else -> MockResponse().apply { addResponse("user_response.json") }
                }
            }
        }

        val timeOutDispatchers: Dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                print(request.path)
                return when (request.path) {
                    else -> MockResponse().apply { addResponse("user_response.json", 6) }
                }
            }
        }

        val errorDispatchers: Dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                print(request.path)
                return when (request.path) {
                    else -> MockResponse().setResponseCode(404)
                }
            }
        }
    }

    val mockWebServer = MockWebServer().apply {
        url("/")
    }

    val userApiServices: UserApiServices = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(UserApiServices::class.java)


}

fun MockResponse.addResponse(filePath: String, delay: Long = 0): MockResponse {
    val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
    val source = inputStream?.source()?.buffer()
    source?.let {
        setBodyDelay(delay, TimeUnit.SECONDS)
        setResponseCode(200)
        setBody(it.readString(StandardCharsets.UTF_8))
    }
    return this
}