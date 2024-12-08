package com.example.beatwell.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.beatwell.R
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.data.Result
import com.example.beatwell.databinding.ActivityRegisterBinding
import com.example.beatwell.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private val viewModel: SignUpViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAnimation()

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val verifyPassword = binding.edVerifyPassword.text.toString()
            if (password == verifyPassword) {
                registerUser(name, email, password)
            } else {
                showToast(getString(R.string.text_password_doesnt_match))
            }
        }

        binding.textLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
    private fun setAnimation() {
        ObjectAnimator.ofFloat(binding.appLogo,View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val tagline = ObjectAnimator.ofFloat(binding.appTagline, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.textName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.textEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.textPassword, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.textLogin, View.ALPHA, 1f).setDuration(500)
        val passwordLayout = ObjectAnimator.ofFloat(binding.passwordLayout, View.ALPHA, 1f).setDuration(500)
        val emailLayout = ObjectAnimator.ofFloat(binding.emailLayout, View.ALPHA, 1f).setDuration(500)
        val nameLayout = ObjectAnimator.ofFloat(binding.nameLayout, View.ALPHA, 1f).setDuration(500)
        val verifyPasswordLayout = ObjectAnimator.ofFloat(binding.verifyPasswordLayout, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(name, nameLayout, email, emailLayout, password, passwordLayout, verifyPasswordLayout)
            start()
        }

        AnimatorSet().apply {
            playSequentially(tagline, together, register, login)
            start()
        }
    }
    private fun registerUser(name: String, email: String, password: String) {
        viewModel.register(name, email, password).observe(this) {result->
            when(result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    showToast(getString(R.string.register_success))
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        result.error,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.textError.visibility = View.VISIBLE
                    binding.textError.text = "email sudah digunakan"
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}