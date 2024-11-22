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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)

        binding.btnPredict.setOnClickListener {
            startActivity(Intent(requireContext(), PredictActivity::class.java))
        }

        return binding.root
    }
}