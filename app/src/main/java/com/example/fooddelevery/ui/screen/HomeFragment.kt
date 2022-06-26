package com.example.fooddelevery.ui.screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fooddelevery.ui.activity.MealActivity
import com.example.fooddelevery.adapter.CategoriesAdapter
import com.example.fooddelevery.adapter.MostPopularAdapter
import com.example.fooddelevery.data.local.room.MealDataBase
import com.example.fooddelevery.data.model.response.Meal
import com.example.fooddelevery.databinding.FragmentHomeBinding
import com.example.fooddelevery.ui.screen.bottomSheet.BottomSheet
import com.example.fooddelevery.view_model.HomeViewModel
import com.example.fooddelevery.view_model.HomeViewModelFactory

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var randomMeal: Meal
    private lateinit var popularAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    lateinit var viewModel: HomeViewModel

    companion object {
        val MEAL_ID = "com.example.fooddelevery.ui.MEAL_ID"
        val MEAL_NAME = "com.example.fooddelevery.ui.MEAL_NAME"
        val MEAL_THUMB = "com.example.fooddelevery.ui.MEAL_THUMB"
    }

    init {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel =
            ViewModelProviders.of(
                requireActivity(),
                HomeViewModelFactory(MealDataBase.INSTANCE!!)
            )[HomeViewModel::class.java]

        popularAdapter = MostPopularAdapter()
        categoriesAdapter = CategoriesAdapter()
        onPopularOnItemLongClickListener()
    }

    private fun onPopularOnItemLongClickListener() {
        popularAdapter.onLongItemClickListener = { meal ->
            val mealBottomSheetFragmen = BottomSheet.newInstance(meal.idMeal)
            mealBottomSheetFragmen.show(childFragmentManager, "Meal info")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularItemsRecyclerView()

        viewModel.getRandomMeal()
        observerMeal()
        randomMealClick()

        viewModel.getPopularItems()
        observerPopularLivedata()
        onPopularOnItemClickListener()

        prepareCategoryRecyclerView()
        viewModel.getCategories()
        observerCategoriesList()


    }

    private fun prepareCategoryRecyclerView() {
        binding.recyclerViewCategories.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observerCategoriesList() {
        viewModel.getObserverLiveDataCategories().observe(viewLifecycleOwner) {
            categoriesAdapter.submitList(it.categories as ArrayList)
        }

    }

    private fun onPopularOnItemClickListener() {
        popularAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            requireActivity().apply {
                startActivity(intent)
            }

        }
    }

    private fun popularItemsRecyclerView() {

        binding.recyViewPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularAdapter
        }
    }

    private fun observerPopularLivedata() {

        viewModel.getObserverLiveDataPopularItems().observe(viewLifecycleOwner) { mealList ->
            popularAdapter.setMealList(mealList as ArrayList)

        }

    }

    private fun randomMealClick() {

        binding.cardViewMealRandom.setOnClickListener {
            requireActivity().run {
                val intent = Intent(this, MealActivity::class.java)

                intent.putExtra(MEAL_ID, randomMeal.idMeal)
                intent.putExtra(MEAL_NAME, randomMeal.strMeal)
                intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
                startActivity(intent)
            }
        }
    }

    private fun observerMeal() {
        viewModel.getObserverLiveDataRandomMeal()
            .observe(viewLifecycleOwner) {
                Glide.with(this@HomeFragment).load(it.strMealThumb).into(binding.imgRandomMeal)
                this.randomMeal = it
            }
    }
}