package com.example.beatwell.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.beatwell.MainActivity
import com.example.beatwell.R
import com.example.beatwell.databinding.ActivitySignInBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.data.Result
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private val viewModel: SignInViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.getSession().observe(this@SignInActivity) { user ->
                if (user.isLogin) {
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    finish()
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        Log.d("SignInActivity", "onCreate: ${result.data}")
                    }

                    is Result.Error -> {
                        Log.d("SignInActivity", "onCreate: ${result.error}")
                    }

                    is Result.Loading -> {
                        Log.d("SignInActivity", "onCreate: Loading")
                    }
                }
            }
        }
    }
}