package com.example.beatwell.ui.food

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beatwell.databinding.FragmentFoodBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.data.Result
import com.example.beatwell.data.remote.response.DataItem

class FoodFragment : Fragment() {

    private val viewModel by viewModels<FoodViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentFoodBinding
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentFoodBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)

        binding.rvFood.layoutManager = layoutManager
        binding.rvFood.addItemDecoration(itemDecoration)

        viewModel.getFoods().observe(viewLifecycleOwner){ result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    setFoodsData(result.data.body.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        return binding.root
    }

    private fun setFoodsData(data: List<DataItem>) {
        adapter = FoodAdapter()
        adapter.submitList(data)
        binding.rvFood.adapter = adapter
    }
}