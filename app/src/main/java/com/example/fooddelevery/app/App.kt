package com.example.fooddelevery.app

import android.app.Application
import com.example.fooddelevery.data.local.room.MealDataBase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MealDataBase.getInstance(this)
    }
}