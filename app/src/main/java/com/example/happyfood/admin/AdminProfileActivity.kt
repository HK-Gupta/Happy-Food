package com.example.happyfood.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.happyfood.R
import com.example.happyfood.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            Toast.makeText(this, "Information Saved Successfully.", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}