package com.example.beatwell.ui.foodDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.beatwell.data.Result
import com.example.beatwell.data.remote.response.FoodData
import com.example.beatwell.databinding.ActivityFoodDetailBinding
import com.example.beatwell.ui.ViewModelFactory

class FoodDetailActivity : AppCompatActivity() {

    private val viewModel: FoodDetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityFoodDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("FoodDetailActivity", "onCreate detail food dipanggil")

        val bundle = intent.extras
        if (bundle != null) {
            val id = bundle.getInt("id")
            id.let{
                viewModel.getFoodDetail(id).observe(this){result->
                    when(result) {
                        is Result.Loading ->{
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success ->{
                            binding.progressBar.visibility = View.GONE
                            val data = result.data.foodData
                            Log.d("FoodDetailActivity", "makananyaaaaa: $data")
                            setFoodDetail(data)
                        }
                        is Result.Error ->{
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }

    }

    private fun setFoodDetail(data: List<FoodData>) {
        Log.d("FoodDetailActivity", "setFoodDetail dipanggil")
        Glide.with(this)
            .load(data[0].image)
            .into(binding.ivDetailFood)
        binding.apply {
            val recipeStrings = data[0].recipe.mapIndexed { index, recipe ->
                "${index + 1}. $recipe"
            }.joinToString(separator = "\n")
            tvRecipe.text = recipeStrings

            val ingredientStrings = data[0].ingredient.mapIndexed { index, ingredient ->
                "${index + 1}. $ingredient"
            }.joinToString(separator = "\n")
            tvIngredients.text = ingredientStrings
            tvFoodName.text = data[0].name
        }
    }


}