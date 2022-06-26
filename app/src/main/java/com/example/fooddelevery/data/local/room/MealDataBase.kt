package com.example.fooddelevery.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fooddelevery.data.model.response.Meal


@Database(entities = [Meal::class], version = 1)
@TypeConverters(MealTypeConverter::class)
abstract class MealDataBase : RoomDatabase() {
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        var INSTANCE: MealDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MealDataBase {
            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(context, MealDataBase::class.java, "app.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()
            }
            return INSTANCE as MealDataBase
        }

    }
}