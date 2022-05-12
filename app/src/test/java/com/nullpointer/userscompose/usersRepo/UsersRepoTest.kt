package com.nullpointer.userscompose.usersRepo

import com.nullpointer.userscompose.core.utils.ServerTimeOut
import com.nullpointer.userscompose.usersRepo.data.local.MockUserDao
import com.nullpointer.userscompose.usersRepo.data.local.MockUserLocalDataSource
import com.nullpointer.userscompose.usersRepo.data.remote.MockUserRemoteDataSource
import com.nullpointer.userscompose.usersRepo.data.remote.UsersFakeServices
import com.nullpointer.userscompose.usersRepo.data.remote.UsersFakeServices.Companion.successDispatchers
import com.nullpointer.userscompose.usersRepo.data.remote.UsersFakeServices.Companion.timeOutDispatchers
import com.nullpointer.userscompose.usersRepo.repository.MockUserRepository
import com.nullpointer.userscompose.usersRepo.utils.CoroutineTestRule
import com.nullpointer.userscompose.usersRepo.utils.listFakeUser
import com.nullpointer.userscompose.usersRepo.utils.userFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersRepoTest {

    private lateinit var mockUserRepository: MockUserRepository
    private lateinit var mockWebServer:UsersFakeServices

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @After
    fun tearDown() {
        mockWebServer.mockWebServer.shutdown()
    }

    @Before
    fun setUp() {
        mockWebServer= UsersFakeServices()
        mockWebServer.mockWebServer.dispatcher = successDispatchers
        val localDataSource = MockUserLocalDataSource(MockUserDao())
        val remoteDataSource = MockUserRemoteDataSource(mockWebServer.userApiServices)
        mockUserRepository = MockUserRepository(remoteDataSource, localDataSource)
    }


    @Test
    fun `Users is fetched correctly`() = runTest {
        mockWebServer.mockWebServer.dispatcher = successDispatchers
        val newUser = withContext(Dispatchers.IO) {
            mockUserRepository.addNewUser()
        }
        assertNotNull(newUser)
        val list = mockUserRepository.listUsers.first()
        assertEquals(list.size, listFakeUser.size + 1)
    }


    @Test(expected = ServerTimeOut::class)
    fun `Users is fetched failed for timeout`() = runTest {
        mockWebServer.mockWebServer.dispatcher = timeOutDispatchers
        val newUser = withContext(Dispatchers.IO) {
            mockUserRepository.addNewUser()
        }
        assertNotNull(newUser)
        val list = mockUserRepository.listUsers.first()
        assertEquals(list.size, listFakeUser.size + 1)
    }

    @Test
    fun `User on the DB are retrieved correctly`() = runTest {
        val list = mockUserRepository.listUsers.first()
        assertEquals(list.size, listFakeUser.size)
    }

    @Test
    fun `User on the DB deleter correctly`() = runTest {
        val old = mockUserRepository.listUsers.first()
        mockUserRepository.deleterUser(listFakeUser.first())
        val new = mockUserRepository.listUsers.first()
        assertEquals(old.size - 1, new.size)
        assertTrue((old - listFakeUser.first()).containsAll(new))
    }

    @Test
    fun `Deleter all users`() = runTest {
        mockUserRepository.deleterAllUsers()
        val list = mockUserRepository.listUsers.first()
        assertEquals(list.size, 0)
    }

    @Test
    fun `Add new user for fetched users`() = runTest {
        val oldList = mockUserRepository.listUsers.first()
        withContext(Dispatchers.IO) {
            mockUserRepository.addNewUser()
        }
        val userFake = userFake
        val newList = mockUserRepository.listUsers.first()
        assertEquals(oldList.size + 1, newList.size)
        assertTrue(newList.contains(userFake))
        assertTrue((oldList + userFake).containsAll(newList))
    }
}