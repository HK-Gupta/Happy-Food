package com.example.happyfood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.happyfood.databinding.HistoryItemBinding

class HistoryAdapter (private val foodNames:MutableList<String>, private val foodPrices:MutableList<String>, private val foodImages:MutableList<String>, private val context: Context)
    : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = foodNames.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class HistoryViewHolder (private val binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.historyDishName.text = foodNames[position]
            binding.historyDishPrice.text = foodPrices[position]
            val uri = Uri.parse(foodImages[position])
            Glide.with(context).load(uri).into(binding.historyDishImage)

        }


    }
}