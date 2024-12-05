package com.example.beatwell.ui.history

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beatwell.data.entity.HistoryEntity
import com.example.beatwell.databinding.FragmentHistoryBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.utils.dateFormater
import com.example.beatwell.utils.dateNumberFormater
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)

        binding.rvHistory.layoutManager = layoutManager
        binding.rvHistory.addItemDecoration(itemDecoration)

        getAllHistory()

        return binding.root
    }

    private fun setChart(historyItem: List<HistoryEntity>) {

        val lineChart = binding.historyChart

        val entries = historyItem.mapIndexed { index, data ->
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
                    return if (index in historyItem.indices) dateNumberFormater(historyItem[index].date) else ""
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

    private fun getAllHistory() {
        viewModel.getAlHistory().observe(viewLifecycleOwner) { historyItem ->
            setHistoryData(historyItem)
            setChart(historyItem)
            Log.d("HistoryFragment","history: $historyItem")
        }
    }

    private fun setHistoryData(historyItem: List<HistoryEntity>?) {
        adapter = HistoryAdapter()
        adapter.submitList(historyItem)
        binding.rvHistory.adapter = adapter
    }
}
