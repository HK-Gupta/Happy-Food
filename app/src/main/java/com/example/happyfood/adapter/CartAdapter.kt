package com.example.happyfood.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.happyfood.databinding.CartItemBinding
import com.example.happyfood.model.CART_ITEMS
import com.example.happyfood.model.CartModel
import com.example.happyfood.model.MenuItem
import com.example.happyfood.model.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class CartAdapter(private val cartItems: MutableList<String>,
                  private val cartItemsPrice: MutableList<String>,
                  private val cartImage: MutableList<String>,
                  private val cartQuantity: MutableList<Int>,
                  private val requireContext: Context
) : RecyclerView.Adapter<CartAdapter.CartViewHolder> () {

    private val auth = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber) {1}
        cartItemReference = database.reference.child(USER_NODE).child(userId).child(CART_ITEMS)
    }
    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun getUpdatedItemQuantities(): MutableList<Int> {
        val itemQuantities = mutableListOf<Int>()
        itemQuantities.addAll(cartQuantity)
        return itemQuantities
    }

    inner class CartViewHolder(private val binding: CartItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                dishName.text = cartItems[position]
                dishPrice.text = cartItemsPrice[position]
                val quantity = cartQuantity[position]

                val uriString = cartImage[position]
                Log.d("image", "Food Url: $uriString")
                val uri = Uri.parse(uriString)
                Glide.with(requireContext).load(uri).into(dishImage)

                addBtnCart.setOnClickListener {
                    if(itemQuantities[position] < 10) {
                        itemQuantities[position]++
                        cartQuantity[position] = itemQuantities[position]
                        binding.quantity.text = itemQuantities[position].toString()
                    }
                }
                subtractBtnCart.setOnClickListener {
                    if(itemQuantities[position] > 1) {
                        itemQuantities[position]--
                        cartQuantity[position] = itemQuantities[position]
                        binding.quantity.text = itemQuantities[position].toString()
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
            Log.d("Delete", "Trying to delete $position")
            getUniqueKeyAtPosition(position) {key->
                if(key != null) {
                    removeItem(position, key)
                }
            }
        }

        private fun removeItem(position: Int, key: String) {
            cartItemReference.child(key).removeValue().addOnSuccessListener {

                cartItems.removeAt(position)
                cartImage.removeAt(position)
                cartQuantity.removeAt(position)
                cartItemsPrice.removeAt(position)

                itemQuantities = itemQuantities.filterIndexed{index, i ->index != position}.toIntArray()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, cartItems.size)
                Toast.makeText(requireContext, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext, "Failed to delete", Toast.LENGTH_SHORT).show()
            }
        }

        private fun getUniqueKeyAtPosition(position: Int, onComplete:(String?)-> Unit) {
            cartItemReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == position) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}