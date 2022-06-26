package com.example.fooddelevery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fooddelevery.data.model.response.CategoryMeals
import com.example.fooddelevery.data.model.response.Meal
import com.example.fooddelevery.data.model.response.MealsByCategory
import com.example.fooddelevery.databinding.PopularItemsBinding

class MostPopularAdapter : RecyclerView.Adapter<MostPopularAdapter.MyViewHolder>() {

    lateinit var onItemClick: ((CategoryMeals) -> Unit)
    var onLongItemClickListener: ((CategoryMeals) -> Unit)? = null
    private var list: ArrayList<CategoryMeals> = ArrayList()

    inner class MyViewHolder(private val binding: PopularItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick.invoke(list[adapterPosition])
            }
            binding.root.setOnLongClickListener {

                onLongItemClickListener?.invoke(list[adapterPosition])
                true
            }

        }

        fun bind() {

            Glide.with(binding.imgPopularMealItems).load(list[adapterPosition].strMealThumb)
                .into(binding.imgPopularMealItems)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size

    fun setMealList(l: ArrayList<CategoryMeals>) {
        this.list = l
        notifyDataSetChanged()
    }

}