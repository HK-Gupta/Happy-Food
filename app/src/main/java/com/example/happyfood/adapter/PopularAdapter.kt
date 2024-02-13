package com.example.happyfood.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.happyfood.FoodDetailsActivity
import com.example.happyfood.databinding.PopuarItemBinding
import com.example.happyfood.model.MenuItem

class PopularAdapter(private val menuItems: List<MenuItem>, private val requireContext: Context)
    : RecyclerView.Adapter<PopularAdapter.PopularViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopuarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    inner class PopularViewHolder (private val binding: PopuarItemBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    openDetailsActivity(position)
                }
            }
        }

        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]
            val intent = Intent(requireContext, FoodDetailsActivity::class.java).apply {
                putExtra("ItemName", menuItem.foodName)
                putExtra("ItemPrice", menuItem.foodPrice)
                putExtra("ItemDescription", menuItem.foodDescription)
                putExtra("ItemIngredient", menuItem.foodIngredients)
                putExtra("ItemImage", menuItem.foodImage)
            }
            requireContext.startActivity(intent)
        }

        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                dishName.text = menuItem.foodName
                dishPrice.text = menuItem.foodPrice
                val uri: Uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(popularDishImage)
            }

        }

    }

}

