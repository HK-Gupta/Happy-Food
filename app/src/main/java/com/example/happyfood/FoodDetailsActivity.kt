package com.example.happyfood

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.happyfood.databinding.ActivityFoodDetailsBinding
import com.example.happyfood.model.CART_ITEMS
import com.example.happyfood.model.CartModel
import com.example.happyfood.model.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FoodDetailsActivity : AppCompatActivity() {

    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null

    private val binding by lazy {
        ActivityFoodDetailsBinding.inflate(layoutInflater)
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        foodName = intent.getStringExtra("ItemName")
        foodPrice = intent.getStringExtra("ItemPrice")
        foodImage = intent.getStringExtra("ItemImage")
        foodDescription = intent.getStringExtra("ItemDescription")
        foodIngredients = intent.getStringExtra("ItemIngredient")

        binding.apply {
            foodNameDetails.text = foodName
            foodPriceDetails.text = "₹$foodPrice"
            descriptionText.text = foodDescription
            ingredientText.text = foodIngredients
            Glide.with(this@FoodDetailsActivity).load(Uri.parse(foodImage))
                .into(foodImageDetails)

        }
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddToCart.setOnClickListener {
            addItemToCart()
        }

    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = Firebase.auth.currentUser?.uid?: ""

        val cartItem = CartModel(foodName, foodPrice, foodImage, 1)
        database.child(USER_NODE).child(userId).child(CART_ITEMS).push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item Added To Cart Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed To Add Item", Toast.LENGTH_SHORT).show()
        }

    }
}