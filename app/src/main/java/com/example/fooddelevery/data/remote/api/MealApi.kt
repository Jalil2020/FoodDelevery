package com.example.fooddelevery.data.remote.api

import com.example.fooddelevery.data.model.response.CategoryMealList
import com.example.fooddelevery.data.model.response.CategoryMeals
import com.example.fooddelevery.data.model.response.MealsByCategory
import com.example.fooddelevery.data.model.response.RandomMeal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<RandomMeal>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id: String): Call<RandomMeal>

    @GET("filter.php?")
    fun getPopularItems(@Query("c") categoryName: String): Call<CategoryMealList>

    @GET("categories.php")
    fun getCategories(): Call<MealsByCategory>
}