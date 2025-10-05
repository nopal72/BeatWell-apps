package com.example.beatwell.ui.predict

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.beatwell.R
import com.example.beatwell.data.Result
import com.example.beatwell.data.pref.ChdRequest
import com.example.beatwell.databinding.ActivityPredictBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.result.ResultActivity
import com.example.beatwell.utils.getDate

class PredictActivity : AppCompatActivity() {

    private val viewModel: PredictViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityPredictBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSend.setOnClickListener { showConfirmationDialog() }
    }

    private fun setRequest() {
        val male = when (binding.radioGender.checkedRadioButtonId) {
            binding.radioPria.id -> 1
            binding.radioWanita.id -> 0
            else -> 0
        }

//        val currentSmoker = when (binding.radioSmoke.checkedRadioButtonId) {
//            binding.smokeTrue.id -> 1
//            binding.smokeFalse.id -> 0
//            else -> 0
//        }

        val height = binding.edHeight.text.toString().toDoubleOrNull() ?: 0.0
        val weight = binding.edWeight.text.toString().toDoubleOrNull() ?: 0.0
        val bmi = if (height > 0) weight / ((height / 100) * (height / 100)) else 0.0

        val request = ChdRequest(
            male = if (binding.radioGender.checkedRadioButtonId == binding.radioPria.id) 1 else 0,
            age = binding.edAge.text.toString().toIntOrNull() ?: 0,
            currentSmoker = if (binding.radioCigs1.checkedRadioButtonId == binding.radioCigsYes.id) 1 else 0,
            cigsPerDay = binding.edCig.text.toString().toIntOrNull() ?: 0,
            BPMeds = when (binding.radioBPMeds.checkedRadioButtonId) {
                binding.BPMedsTrue.id -> 1
                binding.BPMedsFalse.id -> 0
                else -> 0
            },
            prevalentStroke = when (binding.radioStroke.checkedRadioButtonId) {
                binding.strokeTrue.id -> 1
                binding.strokeFalse.id -> 0
                else -> 0
            },
            prevalentHyp = when (binding.radioPrevalentHyp.checkedRadioButtonId) {
                binding.prevalentHypTrue.id -> 1
                binding.prevalentHypFalse.id -> 0
                else -> 0
            },
            diabetes = when (binding.radioDiabetes.checkedRadioButtonId) {
                binding.diabetesTrue.id -> 1
                binding.diabetesFalse.id -> 0
                else -> 0
            },
            totChol = binding.edChol.text.toString().toDoubleOrNull() ?: 0.0,
            sysBP = binding.edSys.text.toString().toDoubleOrNull() ?: 0.0,
            diaBP = binding.edDia.text.toString().toDoubleOrNull() ?: 0.0,
            BMI = bmi,
            heartRate = binding.edHeartRate.text.toString().toDoubleOrNull() ?: 0.0,
            glucose = binding.edGlucose.text.toString().toDoubleOrNull() ?: 0.0
        )

        Log.d("PredictActivity", "Request: $request")
        predict(request)
    }


    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.text_data_confirmation))
        builder.setMessage(getString(R.string.text_desc_data_confirmation))

        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            setRequest()
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun predict(request: ChdRequest) {
        viewModel.predict(request).observe(this) { result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    val intentResult = Intent(this, ResultActivity::class.java)
                    val bundle = Bundle().apply {
//                        Log.d("PredictActivity", "Result Penyakitnya: ${result.data}")
                        putFloat("result", result.data.riskScore.toFloat())
                        putString("date", getDate().toString())
                    }
                    intentResult.putExtras(bundle)
                    startActivity(intentResult)
                    finish()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Log.e("PredictActivity", "Error ada error: ${result.error}")
                    showToast("ada error"+result.error)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}