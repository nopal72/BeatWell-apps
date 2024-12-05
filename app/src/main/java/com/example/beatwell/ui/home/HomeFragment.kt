package com.example.beatwell.ui.home

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.beatwell.R
import com.example.beatwell.data.Result
import com.example.beatwell.data.remote.response.Activity
import com.example.beatwell.databinding.FragmentHomeBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.chatbot.ChatBotActivity
import com.example.beatwell.ui.predict.PredictActivity
import com.example.beatwell.utils.dateFormater

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnPredict.setOnClickListener {
            startActivity(Intent(requireContext(), PredictActivity::class.java))
        }
        binding.btnChatBot.setOnClickListener{
            startActivity(Intent(requireContext(), ChatBotActivity::class.java))
        }
        binding.refresh.setOnRefreshListener {
            setHistory()
            setActivity()
            setTrivia()
            binding.refresh.isRefreshing = false
        }

        setHistory()
        setTrivia()
        setActivity()

        return binding.root
    }

    private fun setActivity() {
        viewModel.getActivity().observe(viewLifecycleOwner){result->
            when(result){
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    setActivityCard(result.data.activity)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setActivityCard(activity: Activity) {
        Glide.with(requireContext())
            .load(activity.image)
            .into(binding.ivActivity)

        binding.tvActivity.text = activity.name
        binding.tvActivityDescription.text = activity.detail
    }

    private fun setTrivia() {
        viewModel.getTrivia().observe(viewLifecycleOwner) {result->
            when(result){
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvTrivia.text = result.data.triviaData.trivia
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun setHistory() {
        viewModel.getHistory().observe(viewLifecycleOwner) {result->
            when(result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data != null){
                        binding.textDate.text = dateFormater(result.data.date)
                        binding.textResult.text = result.data.prediction.toString()
                    } else {
                        binding.textResult.text = "-"
                        binding.textDate.text = getString(R.string.text_no_data)
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}