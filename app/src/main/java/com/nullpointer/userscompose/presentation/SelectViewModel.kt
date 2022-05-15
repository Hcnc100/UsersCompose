package com.nullpointer.userscompose.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.userscompose.core.delegate.SavableComposeState
import com.nullpointer.userscompose.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    companion object {
        const val KEY_LIST_SAVED = "KEY_LIST_SAVED"
    }

    private var listIdsSelect by SavableComposeState(savedStateHandle,
        KEY_LIST_SAVED,
        emptyList<Long>())
    private val listUsersSelect = mutableListOf<User>()

    val isSelectedEnable get() = listIdsSelect.isNotEmpty()
    val numberSelection get() = listIdsSelect.size

    fun changeItemSelected(item: User) {
        listIdsSelect = if (listIdsSelect.contains(item.id)) {
            item.isSelect = false
            listUsersSelect.remove(item)
            listIdsSelect - item.id!!
        } else {
            item.isSelect = true
            listUsersSelect.add(item)
            listIdsSelect + item.id!!
        }
    }

    fun restoreSelectUsers(listUsers:List<User>){
        listUsers.filter { listIdsSelect.contains(it.id!!) }.onEach {
            it.isSelect=true
        }.let {
            listUsersSelect.addAll(it)
        }
    }

    fun getListSelectionAndClear(): List<Long> {
        val listIdMeasure = listOf(*listIdsSelect.toTypedArray())
        clearSelection()
        return listIdMeasure
    }

    fun clearSelection() {
        listUsersSelect.forEach { it.isSelect = false }
        listIdsSelect = emptyList()
    }
}