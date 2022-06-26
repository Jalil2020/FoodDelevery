package com.example.fooddelevery.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelevery.adapter.FavoritesAdapter
import com.example.fooddelevery.data.local.room.MealDataBase
import com.example.fooddelevery.databinding.FragmentFavoriteBinding
import com.example.fooddelevery.view_model.HomeViewModel
import com.example.fooddelevery.view_model.HomeViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding
    lateinit var viewModel: HomeViewModel
    lateinit var adapder: FavoritesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =
            ViewModelProviders.of(requireActivity(),
                HomeViewModelFactory(MealDataBase.INSTANCE!!))[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observerFavoreries()


        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN or ItemTouchHelper.UP,
            ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.delete(adapder.differ.currentList[position])

                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                ) {
                    viewModel.insertMeal(adapder.differ.currentList[position])
                }.show()
            }

        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)



    }

    private fun prepareRecyclerView() {
        adapder = FavoritesAdapter()
        binding.rvFavorites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = adapder
        }

    }

    private fun observerFavoreries() {

        viewModel.obseverFavoritesMealsLiveData().observe(requireActivity()) {
            adapder.differ.submitList(it)
        }
    }

}