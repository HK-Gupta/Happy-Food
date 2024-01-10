package com.example.happyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyfood.databinding.ActivityFoodDetailsBinding

class FoodDetailsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFoodDetailsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("ItemName")
        val foodPic = intent.getIntExtra("ItemImage", 0)
        binding.foodNameDetails.text = foodName
        binding.foodImageDetails.setImageResource(foodPic)

        binding.btnBack.setOnClickListener {
            finish()
        }


    }
}