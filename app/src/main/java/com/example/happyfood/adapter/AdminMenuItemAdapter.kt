package com.example.happyfood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.happyfood.databinding.ActivityAddItemBinding
import com.example.happyfood.databinding.CartItemBinding
import com.example.happyfood.model.AllMenu
import com.google.firebase.database.DatabaseReference

class AdminMenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference
): RecyclerView.Adapter<AdminMenuItemAdapter.AdminAddItemViewHolder> (){

    private val itemQuantities = IntArray(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAddItemViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminAddItemViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: AdminAddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AdminAddItemViewHolder(private val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantities = itemQuantities[position]
                val menuItem = menuList[position]
                val imageUri = menuItem.foodImage
                val uri = Uri.parse(imageUri)

                dishName.text = menuItem.foodName
                dishPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(dishImage)
                quantity.text = quantities.toString()

                addBtnCart.setOnClickListener {
                    if(itemQuantities[position] < 10) {
                        itemQuantities[position]++;
                        quantity.text = itemQuantities[position].toString()
                    }
                }
                subtractBtnCart.setOnClickListener {
                    if(itemQuantities[position] > 1) {
                        itemQuantities[position]--;
                        quantity.text = itemQuantities[position].toString()
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
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
        }

    }
}