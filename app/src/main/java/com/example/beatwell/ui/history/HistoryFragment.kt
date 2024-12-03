package com.example.beatwell.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beatwell.R
import com.example.beatwell.data.entity.HistoryEntity
import com.example.beatwell.databinding.FragmentHistoryBinding
import com.example.beatwell.ui.ViewModelFactory
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    private fun getAllHistory() {
        viewModel.getAlHistory().observe(viewLifecycleOwner) { historyItem ->
            setHistoryData(historyItem)
        }
    }

    private fun setHistoryData(historyItem: List<HistoryEntity>?) {
        adapter = HistoryAdapter()
        adapter.submitList(historyItem)
        binding.rvHistory.adapter = adapter
    }
}
