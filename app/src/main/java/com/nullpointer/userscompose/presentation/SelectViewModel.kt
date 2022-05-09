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

    private var listUserSelect by SavableComposeState(savedStateHandle,
        KEY_LIST_SAVED,
        emptyList<User>())

    val isSelectedEnable get() = listUserSelect.isNotEmpty()
    val numberSelection get() = listUserSelect.size

    fun changeItemSelected(item: User) {
        listUserSelect = if (listUserSelect.contains(item)) {
            item.isSelect = false
            listUserSelect - item
        } else {
            item.isSelect = true
            listUserSelect + item
        }
    }

    fun getListSelectionAndClear():List<Long>{
        val listIdMeasure=listUserSelect.map { it.id!! }
        clearSelection()
        return listIdMeasure
    }

    fun clearSelection() {
        listUserSelect.forEach { it.isSelect = false }
        listUserSelect = emptyList()
    }
}