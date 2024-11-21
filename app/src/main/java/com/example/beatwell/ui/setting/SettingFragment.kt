package com.example.beatwell.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.beatwell.databinding.FragmentSettingBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.login.LoginActivity

class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.btnSignOut.setOnClickListener {
            viewModel.logOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        return binding.root
    }

}