package com.example.happyfood.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.adapter.OrderDetailsAdapter
import com.example.happyfood.databinding.ActivityOrderDetailsBinding
import com.example.happyfood.model.OrderDetailsModel

class OrderDetailsActivity : AppCompatActivity() {

    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodNames: ArrayList<String> = arrayListOf()
    private var foodPrices: ArrayList<String> = arrayListOf()
    private var foodImages: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()

    private val binding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetailsModel
        receivedOrderDetails.let {
            userName = receivedOrderDetails.userName
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            totalPrice = receivedOrderDetails.totalPrice
            foodNames = receivedOrderDetails.foodNames as ArrayList<String>
            foodImages = receivedOrderDetails.foodImage as ArrayList<String>
            foodQuantity = receivedOrderDetails.foodQuantities as ArrayList<Int>
            foodPrices = receivedOrderDetails.foodPrice as ArrayList<String>
            setUserDetails()
            setAdapter()
        }
    }

    private fun setAdapter() {
        binding.orderDetailsRV.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNames, foodPrices, foodImages, foodQuantity)
        binding.orderDetailsRV.adapter = adapter
    }

    private fun setUserDetails() {
        binding.txtName.text = userName
        binding.phoneNumber.text = phoneNumber
        binding.txtAddress.text = address
        binding.totalAmount.text = "₹ $totalPrice"

    }
}