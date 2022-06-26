package com.example.fooddelevery.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fooddelevery.data.model.response.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertMeal(meal: Meal)

    @Delete
    fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealinformasition")
    fun getAllMeals(): LiveData<List<Meal>>
}