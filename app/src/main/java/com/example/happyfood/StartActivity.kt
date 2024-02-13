package com.example.happyfood

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.example.happyfood.admin.AdminLoginActivity
import com.example.happyfood.admin.AdminMainActivity
import com.example.happyfood.databinding.ActivityStartBinding
import com.example.happyfood.model.ADMIN_USER_NODE
import com.example.happyfood.model.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StartActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonUser.setOnClickListener {
            startActivity(Intent(this@StartActivity, LoginActivity::class.java))
            finish()
        }
        binding.buttonAdmin.setOnClickListener {
            startActivity(Intent(this@StartActivity, AdminLoginActivity::class.java))
            finish()
        }
    }

}


