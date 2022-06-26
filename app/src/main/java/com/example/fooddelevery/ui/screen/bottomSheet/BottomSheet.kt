package com.example.fooddelevery.ui.screen.bottomSheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.fooddelevery.R
import com.example.fooddelevery.data.local.room.MealDataBase
import com.example.fooddelevery.databinding.FragmentBottomSheetBinding
import com.example.fooddelevery.ui.activity.MealActivity
import com.example.fooddelevery.ui.screen.HomeFragment
import com.example.fooddelevery.view_model.HomeViewModel
import com.example.fooddelevery.view_model.HomeViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class BottomSheet : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var mealId: String? = null
    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }

        viewModel =
            ViewModelProviders.of(
                requireActivity(),
                HomeViewModelFactory(MealDataBase.INSTANCE!!)
            )[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealById(it) }

        observeBotomSheetMeal()
        bottomSheetDialogClick()
    }

    private fun bottomSheetDialogClick() {

        binding.bottomSheetLayout.setOnClickListener {

            if (mealName != null && mealThumb != null) {
                var bundle = Bundle()
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }


        }
    }

    private var mealName: String? = null
    private var mealThumb: String? = null


    private fun observeBotomSheetMeal() {
        viewModel.obseverBottomSheetMealsLiveData().observe(viewLifecycleOwner) {
            Glide.with(this).load(it.strMealThumb).into(binding.imgBottomSheet)
            binding.txtBottomSheetArea.text = it.strArea
            binding.txtBottomSheetCategory.text = it.strCategory
            binding.bottomSheetMealName.text = it.strMeal

            mealName = it.strMeal
            mealThumb = it.strMealThumb

        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            BottomSheet().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}