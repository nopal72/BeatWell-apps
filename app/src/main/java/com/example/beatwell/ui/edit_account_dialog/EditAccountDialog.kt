package com.example.beatwell.ui.edit_account_dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.beatwell.databinding.EditProfileDialogBinding
import com.example.beatwell.ui.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditAccountDialog: DialogFragment() {

    private lateinit var binding: EditProfileDialogBinding
    private val viewModel: EditAccountDialogViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditProfileDialogBinding.inflate(inflater, container, false)

        binding.btnCancel.setOnClickListener { dismiss() }
        binding.btnSend.setOnClickListener { updateAccount() }

        setValue()
        return binding.root
    }

    private fun setValue() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getUser().collect{user->
                withContext(Dispatchers.Main){
                    binding.edRegisterName.setText(user.name)
                    binding.edRegisterEmail.setText(user.email)
                }
            }
        }
    }

    private fun updateAccount() {
        TODO("Not yet implemented")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}