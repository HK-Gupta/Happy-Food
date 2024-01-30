package com.example.happyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.adapter.RecentBuyAdapter
import com.example.happyfood.databinding.ActivityRecentOrderItemsBinding
import com.example.happyfood.databinding.RecentBuyItemBinding
import com.example.happyfood.model.OrderDetailsModel

class RecentOrderItems : AppCompatActivity() {

    private val binding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodNames: ArrayList<String>
    private lateinit var allFoodPrices: ArrayList<String>
    private lateinit var allFoodImages: ArrayList<String>
    private lateinit var allFoodQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val recentOrderItems = intent.getSerializableExtra("RecentBuyItems") as ArrayList<OrderDetailsModel>
        recentOrderItems.let { orderDetails->
            if(orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]
                allFoodNames = recentOrderItem.foodNames as ArrayList<String>
                allFoodPrices = recentOrderItem.foodPrice as ArrayList<String>
                allFoodImages = recentOrderItem.foodImage as ArrayList<String>
                allFoodQuantities = recentOrderItem.foodQuantities as ArrayList<Int>


            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.recentRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this, allFoodNames, allFoodPrices, allFoodImages, allFoodQuantities)
        binding.recentRecyclerView.adapter = adapter
    }
}