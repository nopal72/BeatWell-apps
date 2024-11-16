package com.example.beatwell.ui.signIn

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.beatwell.databinding.FragmentSignInBinding
import com.example.beatwell.ui.ViewModelFactory

class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentSignInBinding
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            val loginUrl = "https://express-prisma-oauth2.vercel.app/auth/login"
            webView.visibility = View.VISIBLE
            webView.webViewClient = object : WebViewClient(){
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    url: String?
                ): Boolean {
                    url?.let {
                        Log.d("SignUpFragment", "Callback URL: $it")
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
            webView.loadUrl(loginUrl)
        }

        return binding.root
    }

}