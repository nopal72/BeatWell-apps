package com.example.beatwell.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beatwell.data.Result
import com.example.beatwell.data.entity.HistoryEntity
import com.example.beatwell.data.remote.response.HistoryItem
import com.example.beatwell.databinding.FragmentHistoryBinding
import com.example.beatwell.ui.ViewModelFactory

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

//        viewModel.getHistory().observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Result.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                is Result.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    setHistoryData(result.data.historyItem)
//                }
//                is Result.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                }
//            }
//        }

        viewModel.getAlHistory().observe(viewLifecycleOwner) { historyItem ->
            setHistoryData(historyItem)
        }

        return binding.root
    }

    private fun setHistoryData(historyItem: List<HistoryEntity>?) {
        adapter = HistoryAdapter()
        adapter.submitList(historyItem)
        binding.rvHistory.adapter = adapter
    }

}