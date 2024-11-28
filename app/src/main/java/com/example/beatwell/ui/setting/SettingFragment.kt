package com.example.beatwell.ui.setting

import android.app.PendingIntent
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.beatwell.databinding.FragmentSettingBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.alarm.AlarmReceiver
import com.example.beatwell.ui.login.LoginActivity

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
            viewModel.logOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        switchSetup()
        getDailyReminder()

        return binding.root
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