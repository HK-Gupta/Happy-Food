package com.example.happyfood.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyfood.FoodDetailsActivity
import com.example.happyfood.databinding.SearchItemBinding

class SearchAdapter(private val items: MutableList<String>, private val prices: MutableList<String>, private val images: MutableList<Int>, private val requireContext: Context) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder> () {

    private val itemClickListener: OnClickListener?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class SearchViewHolder(private val binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root) {
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
        fun bind(position: Int) {
            binding.apply {
                dishName.text = items[position]
                dishPrice.text = prices[position]
                searchDishImage.setImageResource(images[position])
            }
        }

    }
    interface OnClickListener {
        fun onItemClick(position: Int)
    }

}