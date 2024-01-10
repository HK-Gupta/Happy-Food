package com.example.happyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyfood.databinding.CartItemBinding

class CartAdapter(private val items: MutableList<String>, private val prices: MutableList<String>, private val images: MutableList<Int>) : RecyclerView.Adapter<CartAdapter.CartViewHolder> () {

    private val quantities = IntArray(items.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CartViewHolder(private val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                dishName.text = items[position]
                dishPrice.text = prices[position]
                dishImage.setImageResource(images[position])
                quantity.text = quantities[position].toString()

                addBtnCart.setOnClickListener {
                    if(quantities[position] < 10) {
                        quantities[position]++;
                        quantity.text = quantities[position].toString()
                    }
                }
                subtractBtnCart.setOnClickListener {
                    if(quantities[position] > 1) {
                        quantities[position]--;
                        quantity.text = quantities[position].toString()
                    }
                }
                deleteBtn.setOnClickListener {
                    val itemPos = adapterPosition
                    if(itemPos != RecyclerView.NO_POSITION) {
                        deleteItem(itemPos)
                    }
                }
            }
        }

        private fun deleteItem(position: Int) {
            items.removeAt(position)
            images.removeAt(position)
            prices.removeAt(position)

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }
}