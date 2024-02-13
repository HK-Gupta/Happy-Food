package com.example.happyfood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.happyfood.databinding.PendingItemsBinding

class PendingOrderAdapter (
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val foodPrices: MutableList<String>,
    private val foodImages: MutableList<String>,
    private val itemClicked: OnItemClicked
): RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>(){

    interface OnItemClicked {
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchedClickListener(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val binding = PendingItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun getItemCount(): Int = customerNames.size

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PendingOrderViewHolder(private val binding: PendingItemsBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                totalPrice.text = "Total Price ${foodPrices[position]}"
                val uri = Uri.parse(foodImages[position])
                Glide.with(context).load(uri).into(foodImage)

                acceptOrder.apply {
                    if (!isAccepted) {
                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if(!isAccepted) {
                            text = "Dispatch"
                            isAccepted = true
                            Toast.makeText(context, "Order is Accepted", Toast.LENGTH_SHORT).show()
                            itemClicked.onItemAcceptClickListener(position)
                        } else {
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            Toast.makeText(context, "Order is Dispatched", Toast.LENGTH_SHORT).show()
                            itemClicked.onItemDispatchedClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }
        }

    }
}