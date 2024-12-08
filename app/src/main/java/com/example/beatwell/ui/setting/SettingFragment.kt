package com.example.beatwell.ui.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.beatwell.R
import com.example.beatwell.data.Result
import com.example.beatwell.databinding.FragmentSettingBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.alarm.AlarmReceiver
import com.example.beatwell.ui.edit_account_dialog.EditAccountDialog
import com.example.beatwell.ui.splash.OnBoarding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentSettingBinding
    private lateinit var alarmReceiver: AlarmReceiver

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission())
    {   isGranted: Boolean->
        if(isGranted){
            Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Notification permission rejected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        alarmReceiver = AlarmReceiver()

        if(Build.VERSION.SDK_INT > 32){
            requestPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        switchSetup()
        getDailyReminder()
        settingLanguage()
        settingUser()
        setEditDialog()
        setDeleteUser()
        setLogOut()

        return binding.root
    }

    private fun setLogOut() {
        binding.btnSignOut.setOnClickListener { showConfirmationLogout() }
    }

    private fun setDeleteUser() {
        binding.btnDeleteAccount.setOnClickListener { showConfirmationDelete() }

    }

    private fun showConfirmationDelete() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi Hapus Akun")
        builder.setMessage("Apakah anda yakin ingin menghapus akun?")

        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            viewModel.deleteAccount().observe(viewLifecycleOwner) {result->
                when(result){
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        viewModel.logOut()
                        startActivity(Intent(requireContext(), OnBoarding::class.java))
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(result.error)
                    }
                }
            }
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun setEditDialog() {
        binding.btnAccount.setOnClickListener {
            val dialog = EditAccountDialog()
            dialog.show(parentFragmentManager, "editAccount")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showConfirmationLogout() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.text_confirm_exit))
        builder.setMessage(getString(R.string.text_desc_confirm_exit))

        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            viewModel.logOut()
            startActivity(Intent(requireContext(), OnBoarding::class.java))
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
                getString(R.string.text_time_to_sleep)
            )
        }
        else{
            alarmReceiver.cancelAlarm(requireContext())
        }
    }

}