package com.example.happyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyfood.Fragments.CongratsBottomSheet
import com.example.happyfood.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.orderPlaced.setOnClickListener {
            CongratsBottomSheet().show(supportFragmentManager, "Test")
        }
    }
}