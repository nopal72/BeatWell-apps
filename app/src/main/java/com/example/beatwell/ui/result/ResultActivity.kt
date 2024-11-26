package com.example.beatwell.ui.result

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.beatwell.databinding.ActivityResultBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.data.Result
import com.example.beatwell.data.entity.HistoryEntity

class ResultActivity : AppCompatActivity() {

    private val viewModel: ResultViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null){
            val id = bundle.getInt("id")
            getResult(id)
        }
    }

    private fun getResult(id: Int) {
        viewModel.getResult(id).observe(this) {result ->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val history = result.data
                    setData(history)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setData(history: HistoryEntity) {
        binding.tvChol.text = history.totChol.toString()
        binding.tvAge.text = history.age.toString()
        binding.tvBmi.text = history.bmi.toString()
        binding.tvCigs.text = history.cigsPerDay.toString()
        binding.tvDiabetes.text = history.diabetes.toString()
        binding.tvBPMeds.text = history.bpMeds.toString()
        binding.tvHeartRate.text = history.heartRate.toString()
        binding.tvGlucose.text = history.glucose.toString()
        binding.tvDiaBP.text = history.diaBP.toString()
        binding.tvSysBP.text = history.sysBP.toString()
        binding.tvPrevalentHyp.text = history.prevalentHyp.toString()
        binding.tvPrevalentStroke.text = history.prevalentStroke.toString()
        binding.tvGender.text = history.gender
        binding.tvPredict.text = history.prediction.toString()
    }
}