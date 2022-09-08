package com.nullpointer.userscompose.presentation

import android.accounts.NetworkErrorException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.states.Resource
import com.nullpointer.userscompose.core.utils.launchSafeIO
import com.nullpointer.userscompose.domain.users.UsersRepository
import com.nullpointer.userscompose.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepo: UsersRepository
) : ViewModel() {


    private var jobAddUser: Job? = null

    var isProcessing by mutableStateOf(false)
    private set

    private val _messageErrorProcess = Channel<Int>()
    val messageErrorProcess = _messageErrorProcess.receiveAsFlow()


    val listUsers = flow<Resource<List<User>>> {
        userRepo.listUsers.collect{
            emit(Resource.Success(it))
        }
    }.catch {
        _messageErrorProcess.trySend(R.string.error_users_database)
        Timber.e("Error get users of database $it")
        emit(Resource.Failure)
    }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        Resource.Loading
    )


    fun addNewUser() {
        jobAddUser = launchSafeIO(
            blockBefore = { isProcessing = true },
            blockAfter = { isProcessing = false },
            blockIO = { userRepo.addNewUser() },
            blockException = { sendMessageException(it) })
    }

    private fun sendMessageException(exception: Exception) {
        when (exception) {
            is NetworkErrorException -> _messageErrorProcess.trySend(R.string.error_internet)
            is NullPointerException -> _messageErrorProcess.trySend(R.string.server_time_now)
            else -> {
                _messageErrorProcess.trySend(R.string.error_unknow)
                Timber.e("Unknown error: $exception")
            }
        }
    }

    fun cancelAddNewUser() {
        _messageErrorProcess.trySend(R.string.action_stop_add_user)
        jobAddUser?.cancel()
    }

    fun deleterUser(user: User) = launchSafeIO {
        userRepo.deleterUser(user)
    }

    fun deleterUser(list: List<Long>) = launchSafeIO {
        userRepo.deleterUserByIds(list)
    }

    fun deleterAllUsers() = launchSafeIO {
        userRepo.deleterAllUsers()
    }
}