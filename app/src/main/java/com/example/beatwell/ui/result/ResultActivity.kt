package com.example.beatwell.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.beatwell.MainActivity
import com.example.beatwell.R
import com.example.beatwell.databinding.ActivityResultBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.data.Result
import com.example.beatwell.data.entity.HistoryEntity
import com.example.beatwell.ui.history.HistoryFragment

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
        binding.btnNext.setOnClickListener { moveToHistory() }
    }

    private fun moveToHistory() {
        startActivity(Intent(this, MainActivity::class.java))
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
        binding.tvPredict.text = getString(R.string.prediction_result, history.prediction.toString())
    }
}