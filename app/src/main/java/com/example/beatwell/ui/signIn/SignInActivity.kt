package com.example.beatwell.ui.signIn

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.beatwell.R
import com.example.beatwell.databinding.ActivitySignInBinding
import com.example.beatwell.databinding.FragmentSignInBinding
import com.example.beatwell.ui.ViewModelFactory

class SignInActivity : AppCompatActivity() {

    private val viewModel: SignInViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignInBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}