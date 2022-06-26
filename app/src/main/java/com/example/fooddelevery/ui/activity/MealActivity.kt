package com.example.fooddelevery.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.fooddelevery.R
import com.example.fooddelevery.data.local.room.MealDataBase
import com.example.fooddelevery.data.model.response.Meal
import com.example.fooddelevery.databinding.ActivityMealBinding
import com.example.fooddelevery.ui.screen.HomeFragment
import com.example.fooddelevery.view_model.MealVIewModel
import com.example.fooddelevery.view_model.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealID: String
    private lateinit var mealName: String
    private lateinit var mealPicture: String
    private lateinit var mealYotubeLink: String

    private lateinit var viewModel: MealVIewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMealInfoFromIntent()


        setInformation()

        loadingCase()

        val viewModelFactory = MealDataBase.INSTANCE?.let { MealViewModelFactory(it) }
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MealVIewModel::class.java]
        viewModel.getMealDetails(mealID)
        observerMealDetails()

        clickYoutubeImage()
        onFavateryCLick()

    }

    private fun onFavateryCLick() {

        binding.btnAddToFav.setOnClickListener {
            meal?.let {
                viewModel.insertMeal(it)
                Toast.makeText(this, "Meal seved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clickYoutubeImage() {
        binding.imgYotube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mealYotubeLink))
            startActivity(intent)
        }

    }

    private var meal: Meal? = null
    private fun observerMealDetails() {
        viewModel.observerMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(t: Meal) {
                onResponseCase()
                meal = t
                binding.tvCategories.text = "Category : ${t.strCategory}"
                binding.tvArea.text = "Area : ${t.strArea}"

                binding.tvDetails.text = t.strInstructions
                mealYotubeLink = t.strYoutube.toString()
            }

        })

    }

    private fun setInformation() {
        Glide.with(this).load(mealPicture).into(binding.imgMealDetails)

        binding.collapsingToolBar.title = mealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(R.color.white))

    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealID = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealPicture = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun loadingCase() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.txtInstruction.visibility = View.INVISIBLE
        binding.tvCategories.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYotube.visibility = View.INVISIBLE
    }

    private fun onResponseCase() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.txtInstruction.visibility = View.VISIBLE
        binding.tvCategories.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYotube.visibility = View.VISIBLE
    }
}