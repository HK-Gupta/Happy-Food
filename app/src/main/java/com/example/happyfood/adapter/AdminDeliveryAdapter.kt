package com.example.happyfood.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyfood.databinding.DeliveryItemBinding

class AdminDeliveryAdapter(private val customerNames: MutableList<String>, private val paymentsStatus: MutableList<Boolean>): RecyclerView.Adapter<AdminDeliveryAdapter.DeliveryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerNames.size
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]

                if(paymentsStatus[position])  {
                    paymentStatus.text = "Received"
                } else {
                    paymentStatus.text = "Not Received"
                }
                val colorMap = mapOf(true to Color.GREEN, false to Color.RED)
                paymentStatus.setTextColor(colorMap[paymentsStatus[position]]?:Color.GRAY)
                deliveryStatus.backgroundTintList = ColorStateList.valueOf(colorMap[paymentsStatus[position]]?:Color.GRAY)
            }
        }


    }
}