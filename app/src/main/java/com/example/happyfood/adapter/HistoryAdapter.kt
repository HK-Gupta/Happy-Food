package com.example.happyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyfood.databinding.HistoryItemBinding

class HistoryAdapter (private val foodNames:List<String>, private val prices:List<String>, private val images:List<Int>)
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
            binding.historyDishPrice.text = prices[position]
            binding.historyDishImage.setImageResource(images[position])
        }


    }
}