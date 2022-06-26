package com.example.fooddelevery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fooddelevery.data.model.response.Meal
import com.example.fooddelevery.databinding.FavItemsBinding

class FavoritesAdapter : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {
    inner class ViewHolder(private var binding: FavItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val meal = differ.currentList[adapterPosition]
            Glide.with(binding.imgCategory).load(meal.strMealThumb)
                .into(binding.imgCategory)
            binding.tvCategoriesName.text = meal.strMeal
        }

    }

    private var diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean =
            oldItem.idMeal == newItem.idMeal

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean = oldItem.idMeal == newItem.idMeal

    }
    var differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        FavItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = differ.currentList.size
}