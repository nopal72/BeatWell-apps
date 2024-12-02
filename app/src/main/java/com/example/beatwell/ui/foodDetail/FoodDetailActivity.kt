package com.example.beatwell.ui.foodDetail

import android.os.Bundle
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

    private fun setFoodDetail(data: FoodData) {
        Glide.with(this)
            .load(data.image)
            .into(binding.ivDetailFood)
        binding.apply {
            val recipeStrings = data.recipe.mapIndexed { index, recipe ->
                "${index + 1}. $recipe"
            }.joinToString(separator = "\n")
            tvRecipe.text = recipeStrings

            val ingredientStrings = data.ingredient.mapIndexed { index, ingredient ->
                "${index + 1}. $ingredient"
            }.joinToString(separator = "\n")
            tvIngredients.text = ingredientStrings
            tvFoodName.text = data.name
        }
    }


}