package com.example.happyfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyfood.admin.AdminLoginActivity
import com.example.happyfood.admin.AdminMainActivity
import com.example.happyfood.databinding.ActivityStartBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val auth = Firebase.auth

        binding.buttonUser.setOnClickListener {
            if (auth.currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        binding.buttonAdmin.setOnClickListener {
            if (auth.currentUser != null) {
                startActivity(Intent(this, AdminMainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, AdminLoginActivity::class.java))
            }
        }
    }
}