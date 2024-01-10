package com.example.happyfood.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyfood.FoodDetailsActivity
import com.example.happyfood.databinding.PopuarItemBinding

class PopularAdapter(private val items:List<String>, private val prices:List<String>, private val images:List<Int>, private val requireContext: Context)
    : RecyclerView.Adapter<PopularAdapter.PopularViewHolder> (){

    private val itemClickListener: OnClickListener ?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopuarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val image = images[position]
        val price = prices[position]
        holder.bind(item, image, price)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class PopularViewHolder (private val binding: PopuarItemBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    itemClickListener?.onItemClick(position)
                }
                val intent = Intent(requireContext, FoodDetailsActivity::class.java)
                intent.putExtra("ItemName", items[position])
                intent.putExtra("ItemImage", images[position])
                requireContext.startActivity(intent)
            }
        }
        fun bind(item: String, image: Int, price: String) {
            binding.dishName.text = item
            binding.dishPrice.text = price
            binding.popularDishImage.setImageResource(image)


        }

    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }
}

