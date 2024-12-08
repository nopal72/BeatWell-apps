package com.example.beatwell.ui.edit_account_dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.beatwell.R
import com.example.beatwell.data.Result
import com.example.beatwell.data.pref.EditAccountRequest
import com.example.beatwell.databinding.EditProfileDialogBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.splash.OnBoarding
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
        binding.btnSend.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val confirmPassword = binding.edVerifyPassword.text.toString()
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Konfirmasi Update Akun")
            builder.setMessage("Apakah anda yakin ingin update akun?")

            builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                updateAccount(name, email, password, confirmPassword)
            }

            builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }

        setValue()
        return binding.root
    }

    private fun updateAccount(
        name: String? = null,
        email: String? = null,
        password: String? = null,
        confirmPassword: String? = null
    ) {
        if (password != confirmPassword){
            binding.edVerifyPassword.error = getString(R.string.text_password_doesnt_match)
        }
        else{
            val request = EditAccountRequest(
                name = name?.ifEmpty { null },
                email = email?.ifEmpty { null },
                password = password?.ifEmpty { null }
            )
            viewModel.editAccount(request).observe(viewLifecycleOwner){result->
                when(result){
                    is Result.Loading ->{
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success ->{
                        binding.progressBar.visibility = View.GONE
                        if (result.data.error){
                            binding.textError.visibility = View.VISIBLE
                            binding.textError.text = result.data.message
                        }
                        else{
                            binding.textError.visibility = View.GONE
                            showToast(result.data.message)
                            viewModel.logOut()
                            startActivity(Intent(requireContext(), OnBoarding::class.java))
                            dismiss()
                        }
                    }
                    is Result.Error ->{
                        binding.progressBar.visibility = View.GONE
                        binding.textError.visibility = View.VISIBLE
                        binding.textError.text = result.error
                        showToast(result.error)
                    }
                }
            }
        }
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

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}