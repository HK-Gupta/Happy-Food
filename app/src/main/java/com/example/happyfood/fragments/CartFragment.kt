package com.example.happyfood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.PaymentActivity
import com.example.happyfood.R
import com.example.happyfood.adapter.CartAdapter
import com.example.happyfood.databinding.FragmentCartBinding
import com.example.happyfood.model.CART_ITEMS
import com.example.happyfood.model.CartModel
import com.example.happyfood.model.MenuItem
import com.example.happyfood.model.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    private lateinit var foodName: MutableList<String>
    private lateinit var foodPrice: MutableList<String>
    private lateinit var foodImage: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var adapter: CartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        retrieveCartItems()

        binding.gotoPayment.setOnClickListener {
            getOrderItemDetails()
        }

        return binding.root
    }

    private fun getOrderItemDetails() {
        val orderIdReference = database.reference.child(USER_NODE).child(userId).child(CART_ITEMS)

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()

        val foodQuantity = adapter.getUpdatedItemQuantities()

        orderIdReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    // Taking items from the cart and adding it to list
                    val orderItem = foodSnapshot.getValue (CartModel::class.java)
                    orderItem?.foodName?.let { foodName.add(it) }
                    orderItem?.foodPrice?.let { foodPrice.add(it) }
                    orderItem?.foodImage?.let { foodImage.add(it) }
//                    orderItem?.foodQuantity?.let { foodQuantity.add(it) }
                }
                orderNow(foodName, foodPrice, foodImage, foodQuantity)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to place order. Please try again", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodImage: MutableList<String>,
        foodQuantity: MutableList<Int>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PaymentActivity::class.java)
            intent.putExtra("BuyFoodName", foodName as ArrayList<String>)
            intent.putExtra("BuyFoodPrice", foodPrice as ArrayList<String>)
            intent.putExtra("BuyFoodImage", foodImage as ArrayList<String>)
            intent.putExtra("BuyFoodQuantity", foodQuantity as ArrayList<Int>)
            startActivity(intent)

        }
    }
    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?: ""

        val foodReference = database.reference.child(USER_NODE).child(userId).child(CART_ITEMS)

        // Creating Lost To store Items.
        foodName = mutableListOf()
        foodPrice = mutableListOf()
        foodImage = mutableListOf()
        quantity = mutableListOf()

        foodReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val cartItem = foodSnapshot.getValue(CartModel::class.java)

                    cartItem?.foodName?.let { foodName.add(it) }
                    cartItem?.foodPrice?.let { foodPrice.add(it) }
                    cartItem?.foodImage?.let { foodImage.add(it) }
                    cartItem?.foodQuantity?.let { quantity.add(it) }
                }

                setCartAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setCartAdapter() {
        adapter = CartAdapter(foodName, foodPrice, foodImage, quantity, requireContext())
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartRecyclerView.adapter = adapter
    }

}