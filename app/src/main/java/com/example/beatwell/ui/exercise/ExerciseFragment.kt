package com.example.beatwell.ui.exercise

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beatwell.databinding.FragmentExerciseBinding
import com.example.beatwell.ui.ViewModelFactory
import com.example.beatwell.ui.predict.PredictActivity

class ExerciseFragment : Fragment() {

    private val viewModel: ExerciseViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var binding: FragmentExerciseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentExerciseBinding.inflate(inflater, container, false)

        binding.btnPredict.setOnClickListener {
            startActivity(Intent(requireContext(), PredictActivity::class.java))
        }

        setHistory()

        return binding.root
    }

    private fun setHistory() {
        viewModel.getLastHistory {
            if (it != null) {
                binding.textResult.text = it.prediction.toString()
                binding.textDate.text = it.date
            }
        }
    }
}