package com.example.beatwell.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beatwell.MainViewModel
import com.example.beatwell.data.UserRepository
import com.example.beatwell.di.Injection
import com.example.beatwell.ui.chatbot.ChatBotViewModel
import com.example.beatwell.ui.home.HomeViewModel
import com.example.beatwell.ui.food.FoodViewModel
import com.example.beatwell.ui.foodDetail.FoodDetailViewModel
import com.example.beatwell.ui.history.HistoryViewModel
import com.example.beatwell.ui.result.ResultViewModel
import com.example.beatwell.ui.setting.SettingViewModel
import com.example.beatwell.ui.login.SignInViewModel
import com.example.beatwell.ui.news.NewsViewModel
import com.example.beatwell.ui.predict.PredictViewModel
import com.example.beatwell.ui.register.SignUpViewModel

class ViewModelFactory(private val userRepository: UserRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java)->{
                HomeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(FoodViewModel::class.java)->{
                FoodViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java)->{
                HistoryViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java)->{
                ResultViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java)->{
                SettingViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java)->{
                SignInViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java)->{
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(PredictViewModel::class.java)->{
                PredictViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java)->{
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(FoodDetailViewModel::class.java)->{
                FoodDetailViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java)->{
                NewsViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ChatBotViewModel::class.java)->{
                ChatBotViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}