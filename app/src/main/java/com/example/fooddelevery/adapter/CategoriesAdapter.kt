package com.example.fooddelevery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fooddelevery.data.model.response.Category
import com.example.fooddelevery.data.model.response.Meal
import com.example.fooddelevery.databinding.CategoryItemsBinding

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var list: ArrayList<Category> = ArrayList()

    inner class ViewHolder(private var binding: CategoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            Glide.with(binding.imgCategory).load(list[adapterPosition].strCategoryThumb)
                .into(binding.imgCategory)
            binding.tvCategoriesName.text = list[adapterPosition].strCategory
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        CategoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = list.size

    fun submitList(l: ArrayList<Category>) {
        list = l
        notifyDataSetChanged()
    }
}