package com.example.beatwell.ui.edit_account_dialog

import androidx.lifecycle.ViewModel
import com.example.beatwell.data.UserRepository

class EditAccountDialogViewModel(private val repository: UserRepository) : ViewModel()  {
    fun getUser() = repository.getSession()
}