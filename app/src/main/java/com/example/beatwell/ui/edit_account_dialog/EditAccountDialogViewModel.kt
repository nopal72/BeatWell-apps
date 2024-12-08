package com.example.beatwell.ui.edit_account_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beatwell.data.UserRepository
import com.example.beatwell.data.pref.EditAccountRequest
import kotlinx.coroutines.launch

class EditAccountDialogViewModel(private val repository: UserRepository) : ViewModel()  {
    fun getUser() = repository.getSession()
    fun editAccount(request: EditAccountRequest) = repository.editAccount(request)
    fun logOut() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}