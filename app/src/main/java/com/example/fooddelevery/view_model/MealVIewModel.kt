package com.example.fooddelevery.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelevery.data.local.room.MealDataBase
import com.example.fooddelevery.data.model.response.Meal
import com.example.fooddelevery.data.model.response.RandomMeal
import com.example.fooddelevery.data.remote.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.IDN

class MealVIewModel(private var mealDataBase: MealDataBase) : ViewModel() {
    private var mealDetailsLiveData = MutableLiveData<Meal>()

    fun getMealDetails(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<RandomMeal> {
            override fun onResponse(call: Call<RandomMeal>, response: Response<RandomMeal>) {
                if (response.body() != null) {
                    mealDetailsLiveData.postValue(response.body()!!.meals.get(0))
                } else
                    return
            }

            override fun onFailure(call: Call<RandomMeal>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }

    fun observerMealDetailsLiveData(): LiveData<Meal> {
        return mealDetailsLiveData
    }

    fun insertMeal(meal: Meal) {

            mealDataBase.mealDao().upsertMeal(meal)
    }


}