package com.example.fooddelevery.view_model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddelevery.data.local.room.MealDataBase
import com.example.fooddelevery.data.model.response.*
import com.example.fooddelevery.data.remote.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private var mealDataBase: MealDataBase,
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<CategoryMeals>>()
    private var categoriesLiveData = MutableLiveData<MealsByCategory>()
    private var favoritiesMealsLiveData = mealDataBase.mealDao().getAllMeals()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<RandomMeal> {
            override fun onResponse(call: Call<RandomMeal>, response: Response<RandomMeal>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.postValue(randomMeal)
                } else {
                    return
                }

            }

            override fun onFailure(call: Call<RandomMeal>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }

    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood")
            .enqueue(object : Callback<CategoryMealList> {
                override fun onResponse(
                    call: Call<CategoryMealList>,
                    response: Response<CategoryMealList>,
                ) {
                    if (response.body() != null) {
                        popularItemsLiveData.postValue(response!!.body()!!.meals)
                    } else return

                }

                override fun onFailure(call: Call<CategoryMealList>, t: Throwable) {
                    Log.d("TAG", "onFailure: ${t.message}")
                }

            })
    }

    fun getObserverLiveDataRandomMeal(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun getObserverLiveDataPopularItems(): LiveData<List<CategoryMeals>> {
        return popularItemsLiveData
    }

    fun getObserverLiveDataCategories(): LiveData<MealsByCategory> {
        return categoriesLiveData
    }

    fun getCategories() {
        RetrofitInstance.api.getCategories().enqueue(object : Callback<MealsByCategory> {
            override fun onResponse(
                call: Call<MealsByCategory>,
                response: Response<MealsByCategory>,
            ) {
                response.body()?.let {
                    categoriesLiveData.postValue(it)

                }
            }

            override fun onFailure(call: Call<MealsByCategory>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
            }
        })
    }

    fun obseverFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritiesMealsLiveData
    }

    fun obseverBottomSheetMealsLiveData(): LiveData<Meal> {
        return bottomSheetMealLiveData
    }

    fun delete(meal: Meal) {
        mealDataBase.mealDao().deleteMeal(meal)

    }

    fun insertMeal(meal: Meal) {

        mealDataBase.mealDao().upsertMeal(meal)
    }

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<RandomMeal> {
            override fun onResponse(call: Call<RandomMeal>, response: Response<RandomMeal>) {
                if (response.isSuccessful && response.body() != null) {
                    val meal = response.body()?.meals?.first()

                    meal.let {
                        bottomSheetMealLiveData.postValue(it)
                    }
                }
            }

            override fun onFailure(call: Call<RandomMeal>, t: Throwable) {
                Log.d(TAG, "onFailure: HomeViewModel")
            }
        })
    }
}