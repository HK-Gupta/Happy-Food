package com.example.happyfood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.happyfood.databinding.RecentBuyItemBinding

class OrderDetailsAdapter (
    private val context: Context,
    private val foodNameList: ArrayList<String>,
    private val foodPriceList: ArrayList<String>,
    private val foodImageList: ArrayList<String>,
    private val foodQuantityList: ArrayList<Int>
): RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int = foodNameList.size

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class OrderDetailsViewHolder(private val binding: RecentBuyItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                dishName.text = foodNameList[position]
                dishPrice.text = "₹ ${foodPriceList[position]}"
                quantity.text = "Quantity\n${foodQuantityList[position]}"

                val uri = Uri.parse(foodImageList[position])
                Glide.with(context).load(uri).into(dishImage)
            }
        }

    }
}