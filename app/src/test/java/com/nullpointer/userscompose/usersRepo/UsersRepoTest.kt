package com.nullpointer.userscompose.usersRepo

import com.nullpointer.userscompose.core.utils.ServerTimeOut
import com.nullpointer.userscompose.models.User
import com.nullpointer.userscompose.usersRepo.repository.MockUser
import com.nullpointer.userscompose.usersRepo.utils.CoroutineTestRule
import com.nullpointer.userscompose.usersRepo.utils.listFakeUser
import com.nullpointer.userscompose.usersRepo.utils.userFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.Assert.*
import timber.log.Timber


@OptIn(ExperimentalCoroutinesApi::class)
class UsersRepoTest {
    private val mockUser = MockUser()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @After
    fun tearDown() {
        mockUser.mockWebServer.shutdown()
    }

    @Before
    fun setUp() {
        mockUser.mockWebServer.dispatcher = MockUser.successDispatchers
    }


    @Test
    fun `Users is fetched correctly`() = runTest {
        mockUser.mockWebServer.dispatcher = MockUser.successDispatchers
        val newUser = withContext(Dispatchers.IO) {
            mockUser.userRepository.addNewUser()
        }
        assertNotNull(newUser)
        val list = mockUser.userRepository.listUsers.first()
        assertEquals(list.size, listFakeUser.size + 1)
    }

    @Test(expected = ServerTimeOut::class)
    fun `Users is fetched failed for timeout`() = runTest {
        mockUser.mockWebServer.dispatcher = MockUser.timeOutDispatchers
        val newUser = withContext(Dispatchers.IO) {
            mockUser.userRepository.addNewUser()
        }
        assertNotNull(newUser)
        val list = mockUser.userRepository.listUsers.first()
        assertEquals(list.size, listFakeUser.size + 1)
    }

    @Test
    fun `User on the DB are retrieved correctly`() = runTest {
        val list = mockUser.userRepository.listUsers.first()
        assertEquals(list.size, listFakeUser.size)
    }

    @Test
    fun `User on the DB deleter correctly`() = runTest {
        val old = mockUser.userRepository.listUsers.first()
        mockUser.userRepository.deleterUser(listFakeUser.first())
        val new = mockUser.userRepository.listUsers.first()
        assertEquals(old.size - 1, new.size)
        assertTrue((old - listFakeUser.first()).containsAll(new))
    }

    @Test
    fun `Deleter all users`() = runTest {
        mockUser.userRepository.deleterAllUsers()
        val list = mockUser.userRepository.listUsers.first()
        assertEquals(list.size, 0)
    }

    @Test
    fun `Add new user for fetched users`() = runTest {
        val oldList = mockUser.userRepository.listUsers.first()
        withContext(Dispatchers.IO) {
            mockUser.userRepository.addNewUser()
        }
        val userFake= userFake
        val newList = mockUser.userRepository.listUsers.first()
        assertEquals(oldList.size + 1, newList.size)
        assertTrue(newList.contains(userFake))
        assertTrue((oldList+userFake).containsAll(newList))
    }
}