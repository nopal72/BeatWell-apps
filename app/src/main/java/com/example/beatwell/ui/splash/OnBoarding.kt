package com.example.beatwell.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.beatwell.MainActivity
import com.example.beatwell.databinding.ActivitySplashBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.login.LoginActivity
import com.example.beatwell.ui.register.RegisterActivity
import kotlinx.coroutines.launch

class OnBoarding : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUser()
        binding.btnLogin.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
        binding.btnRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }

    private fun setUser() {
        lifecycleScope.launch {
            viewModel.getSession().observe(this@OnBoarding) { user ->
                if (user.isLogin) {
                    startActivity(Intent(this@OnBoarding, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}