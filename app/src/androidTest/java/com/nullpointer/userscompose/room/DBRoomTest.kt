package com.nullpointer.userscompose.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nullpointer.userscompose.data.local.room.UsersDao
import com.nullpointer.userscompose.data.local.room.UsersDatabase
import com.nullpointer.userscompose.models.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DBRoomTest {

    private lateinit var userDao: UsersDao
    private lateinit var databaseDataSource: UsersDatabase
    private val listUsersFake = User.generateListUsers(100)

    @Before
    fun createDb() {
        // * get database only memory
        val context = ApplicationProvider.getApplicationContext<Context>()
        databaseDataSource = Room.inMemoryDatabaseBuilder(
            context, UsersDatabase::class.java
        ).build()
        // * get dao
        userDao = databaseDataSource.getUsersDao()
    }

    // ! when finish the test close database
    @After
    fun closeDb() {
        databaseDataSource.close()
    }

    fun addListAndAddUserOne() = runBlocking {
        listUsersFake.forEach(userDao::addNewUser)
        val listUsersOne = userDao.getAllUsers().first()

        userDao.deleterAllUsers()

        userDao.addListUsers(listUsersFake)
        val listUsersTwo = userDao.getAllUsers().first()

        assertEquals(listUsersOne.size, listUsersFake)
        assertEquals(listUsersOne.size, listUsersTwo.size)
        assertTrue(listUsersOne.containsAll(listUsersTwo))
        assertTrue(listUsersTwo.containsAll(listUsersOne))
    }

    fun addUsersInDatabase() = runBlocking {
        userDao.addListUsers(listUsersFake)
        val listUsersRoom = userDao.getAllUsers().first()
        assertEquals(listUsersFake.size, listUsersRoom.size)
        assertTrue(listUsersRoom.containsAll(listUsersFake))
    }

    @Test
    fun getUserByIdSuccess() = runBlocking {
        userDao.addListUsers(listUsersFake)
        // * verify exist random user is success insert
        // * and all users insert
        val random = listUsersFake.random()
        val userGet = userDao.getUserById(random.id!!)
        val listUsersRoom = userDao.getAllUsers().first()
        assertEquals(listUsersFake.size, listUsersRoom.size)
        assertTrue(listUsersRoom.containsAll(listUsersFake))
        assertNotNull(userGet)
        assertEquals(random, userGet)
    }

    @Test
    fun getUserByIdFail() = runBlocking {
        userDao.addListUsers(listUsersFake)

        // * range of ids that no exist in database
        val noIdRange = (listUsersFake.size + 10..110)
        repeat(10) {
            val nullUser = userDao.getUserById(noIdRange.random().toLong())
            assertNull(nullUser)
        }
        // * expect no remove users
        val listUsersRoom = userDao.getAllUsers().first()
        assertEquals(listUsersFake.size, listUsersRoom.size)
    }


    @Test
    fun deleterUser() = runBlocking {
        userDao.addListUsers(listUsersFake)

        // * get random user and deleter
        val randomUser = listUsersFake.random()
        userDao.deleterUser(randomUser)

        val listUsers = userDao.getAllUsers().first()
        // * list expected
        val expectList = (listUsersFake - randomUser)
        assertTrue(expectList.size == listUsers.size)
        assertTrue(listUsers.containsAll(expectList))
    }


    @Test
    fun deleterUserById() = runBlocking {
        userDao.addListUsers(listUsersFake)

        val randomUser = listUsersFake.random()
        // * only one random user is true remove the other is random ids
        userDao.deleterUsersById(listOf(randomUser.id!!, 123, 246))

        val listUsers = userDao.getAllUsers().first()
        // * expect list with only random user removed
        val expectedList = (listUsersFake - randomUser)
        assertTrue(expectedList.size == listUsers.size)
        assertTrue(listUsers.containsAll(expectedList))
    }

    @Test
    fun deleterAllUsers() = runBlocking {
        userDao.addListUsers(listUsersFake)
        // * deleter all users
        userDao.deleterAllUsers()
        val listUsers = userDao.getAllUsers().first()
        // * equals empty
        assertTrue(listUsers.isEmpty())
        // * all users get return null
        listUsersFake.forEach {
            assertNull(userDao.getUserById(it.id!!))
        }
    }

    @Test
    fun deleterUserListById() = runBlocking {
        userDao.addListUsers(listUsersFake)
        val listUsersInit = userDao.getAllUsers().first()
        assertEquals(listUsersFake.size, listUsersInit.size)

        val valueRange = (1..listUsersFake.size).random()
        val listUserIdsRandom = mutableSetOf<Long>()
        repeat(valueRange) {
            listUserIdsRandom.add(listUsersFake.random().id!!)
        }


        userDao.deleterUsersById(listUserIdsRandom.toList())
        val listUsersFinal = userDao.getAllUsers().first()
        assertEquals(listUsersFinal.size, listUsersFake.size - listUserIdsRandom.toList().size)
    }
}