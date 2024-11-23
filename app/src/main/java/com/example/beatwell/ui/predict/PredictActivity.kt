package com.example.beatwell.ui.predict

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.beatwell.data.pref.PredictRequest
import com.example.beatwell.databinding.ActivityPredictBinding
import com.example.beatwell.ui.ViewModelFactory

class PredictActivity : AppCompatActivity() {

    private val viewModel: PredictViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityPredictBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val request = PredictRequest(
                sex = when (binding.radioGender.checkedRadioButtonId) {
                    binding.radioPria.id -> "Male"
                    binding.radioWanita.id -> "Female"
                    else -> ""
                },
                age = binding.edAge.text.toString().toIntOrNull() ?: 0,
                cigsPerday = binding.edCig.text.toString().toIntOrNull() ?: 0,
                BPMeds = when (binding.radioBPMeds.checkedRadioButtonId) {
                    binding.BPMedsTrue.id -> true
                    binding.BPMedsFalse.id -> false
                    else -> false
                },
                prevalentStroke = when (binding.radioStroke.checkedRadioButtonId) {
                    binding.strokeTrue.id -> true
                    binding.strokeFalse.id -> false
                    else -> false
                },
                prevalentHyp = when (binding.radioPrevalentHyp.checkedRadioButtonId) {
                    binding.prevalentHypTrue.id -> true
                    binding.prevalentHypFalse.id -> false
                    else -> false
                },
                diabetes = when (binding.radioDiabetes.checkedRadioButtonId) {
                    binding.diabetesTrue.id -> true
                    binding.diabetesFalse.id -> false
                    else -> false
                },
                totChol = binding.edChol.text.toString().toIntOrNull() ?: 0,
                sysBP = binding.edSys.text.toString().toIntOrNull() ?: 0,
                diaBP = binding.edDia.text.toString().toIntOrNull() ?: 0,
                BMI = binding.edBmi.text.toString().toIntOrNull() ?: 0,
                heartRate = binding.edHeartRate.text.toString().toIntOrNull() ?: 0,
                glucose = binding.edGlucose.text.toString().toIntOrNull() ?: 0
            )
            predict(request)
        }

    }

    private fun predict(request: PredictRequest) {
        viewModel.predict(request)
    }
}