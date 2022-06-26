package com.example.fooddelevery.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.fooddelevery.data.local.room.MealDataBase

class MealViewModelFactory(
  private  var mealDataBase: MealDataBase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MealVIewModel(mealDataBase) as T
    }

}