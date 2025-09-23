package com.example.beatwell.ui.result

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.beatwell.MainActivity
import com.example.beatwell.R
import com.example.beatwell.databinding.ActivityResultBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.utils.dateFormater
import com.example.beatwell.utils.dateNumberFormater
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.example.beatwell.data.Result

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
            val date = bundle.getString("date")
//            val newData = HistoryEntity(date = date.toString(), prediction = result)
            setData(result, date.toString())
            setHistoryChart()

            binding.btnSave.setOnClickListener { addHistory(result) }
        }
        binding.btnNext.setOnClickListener { moveToHistory() }
    }

    private fun addHistory(result: Int) {
        viewModel.addHistory(result).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
//                    Log.e("PredictActivity", "Error ada error: ${result.error}")
                    showToast("ada error"+result.error)
                }
                is Result.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    showToast("Berhasil disimpan")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setHistoryChart() {
        viewModel.getHistory().observe(this) { historyItem ->
            val historyItemReversed = historyItem.reversed()
            val lineChart = binding.historyChart
            val entries = historyItemReversed.mapIndexed { index, data ->
                Entry(index.toFloat(), data.prediction.toFloat())
            }
            val dataSet = LineDataSet(entries, "Predictions").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setCircleColor(Color.RED)
                circleRadius = 5f
            }
            lineChart.xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                labelRotationAngle = -75f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index in historyItemReversed.indices) dateNumberFormater(historyItemReversed[index].date) else ""
                    }
                }
            }
            lineChart.axisLeft.apply {
                granularity = 10f
            }
            lineChart.axisRight.isEnabled = false

            lineChart.data = LineData(dataSet)

            lineChart.description.text = "History Prediksi"
            lineChart.invalidate()
        }
    }

    private fun setData(result: Int, date: String) {
        binding.tvPredict.text = getString(R.string.prediction_result, result.toString())
        binding.tvDate.text = dateFormater(date)
    }

    private fun moveToHistory() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}