package com.example.beatwell.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.beatwell.MainActivity
import com.example.beatwell.databinding.ActivityResultBinding
import com.example.beatwell.ui.ViewModelFactory
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
            val result = bundle.getInt("result")
            setData(result)
        }
        binding.btnNext.setOnClickListener { moveToHistory() }
    }

    private fun setData(result: Int) {
        binding.tvPredict.text = result.toString()
    }

    private fun moveToHistory() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}