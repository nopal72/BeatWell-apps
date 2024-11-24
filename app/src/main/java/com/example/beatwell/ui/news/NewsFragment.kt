package com.example.beatwell.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beatwell.data.Result
import com.example.beatwell.data.remote.response.ArticlesItem
import com.example.beatwell.databinding.FragmentNewsBinding
import com.example.beatwell.ui.ViewModelFactory


class NewsFragment : Fragment() {

    private val viewModel: NewsViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentNewsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)

        binding.rvNews.layoutManager = layoutManager
        binding.rvNews.addItemDecoration(itemDecoration)

        setNews()

        return binding.root
    }

    private fun setNews() {
        viewModel.getNews().observe(viewLifecycleOwner){result->
            when(result) {
                is Result.Loading-> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val news = result.data.articles
                    setAdapter(news)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setAdapter(news: List<ArticlesItem>) {
        val adapter = NewsAdapter()
        adapter.submitList(news)
        binding.rvNews.adapter = adapter
    }

}