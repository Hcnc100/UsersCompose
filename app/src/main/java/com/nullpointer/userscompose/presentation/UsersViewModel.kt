package com.nullpointer.userscompose.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.userscompose.R
import com.nullpointer.userscompose.core.states.Resource
import com.nullpointer.userscompose.core.utils.InternetCheckError
import com.nullpointer.userscompose.core.utils.ServerTimeOut
import com.nullpointer.userscompose.domain.users.UsersRepository
import com.nullpointer.userscompose.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
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
        jobAddUser?.cancel()
        jobAddUser = viewModelScope.launch(Dispatchers.IO) {
            isProcessing = true
            delay(1000)
            try {
                userRepo.addNewUser()
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is InternetCheckError -> _messageErrorProcess.trySend(R.string.error_internet)
                    is ServerTimeOut -> _messageErrorProcess.trySend(R.string.server_time_now)
                    else -> {
                        _messageErrorProcess.trySend(R.string.error_unknow)
                        Timber.e("Error desconocido $e")
                    }
                }
            }
            isProcessing = false
        }
    }

    fun cancelAddNewUser() {
        _messageErrorProcess.trySend(R.string.action_stop_add_user)
        jobAddUser?.cancel()
        isProcessing = false
    }

    fun deleterUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepo.deleterUser(user)
    }

    fun deleterUser(list: List<Long>) = viewModelScope.launch(Dispatchers.IO) {
        userRepo.deleterUserByIds(list)
    }

    fun deleterAllUsers() = viewModelScope.launch(Dispatchers.IO) {
        userRepo.deleterAllUsers()
    }
}