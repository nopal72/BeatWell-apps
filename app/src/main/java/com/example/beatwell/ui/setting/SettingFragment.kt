package com.example.beatwell.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.beatwell.R
import com.example.beatwell.databinding.FragmentSettingBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.alarm.AlarmReceiver
import com.example.beatwell.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentSettingBinding
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        alarmReceiver = AlarmReceiver()

        binding.btnSignOut.setOnClickListener {
            showConfirmationDialog()
        }

        switchSetup()
        getDailyReminder()
        settingLanguage()
        settingUser()

        return binding.root
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi Keluar")
        builder.setMessage("Apakah anda yakin ingin keluar?")

        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            viewModel.logOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun settingUser() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getUser().collect{user->
                binding.tvName.text = user.name
                binding.tvEmail.text = user.email
            }
        }
    }

    private fun settingLanguage() {
        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun switchSetup() {
        binding.switchAlarm.setOnCheckedChangeListener {_,isChecked->
            viewModel.setDailyReminder(isChecked)
        }
    }

    private fun getDailyReminder() {
        viewModel.getDailyReminder().observe(viewLifecycleOwner) {isActive->
            binding.switchAlarm.isChecked = isActive
            setDailyReminder(isActive)
        }
    }

    private fun setDailyReminder(isActive: Boolean) {
        if (isActive){
            alarmReceiver.setRepeatingAlarm(
                requireContext(),
                AlarmReceiver.TYPE_REPEATING,
                "Sudah saatnya tidur, yuk beristirahat"
            )
        }
        else{
            alarmReceiver.cancelAlarm(requireContext(), AlarmReceiver.TYPE_REPEATING)
        }
    }

}