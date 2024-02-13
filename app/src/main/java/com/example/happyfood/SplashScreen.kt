package com.example.happyfood

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import com.example.happyfood.admin.AdminMainActivity
import com.example.happyfood.databinding.ActivitySplashScreenBinding
import com.example.happyfood.model.ADMIN_USER_NODE
import com.example.happyfood.model.SHARED_PREFERENCE_KEY
import com.example.happyfood.model.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var isLoggedIn = true
    private lateinit var sharedPreferences: SharedPreferences

    private val binding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPreferences = this@SplashScreen.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
        val isLogin = sharedPreferences.getString(SHARED_PREFERENCE_KEY, "NONE")?: "NONE"


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        val bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        val leftAnimation = AnimationUtils.loadAnimation(this, R.anim.left_animation)
        val rightAnimation = AnimationUtils.loadAnimation(this, R.anim.right_animation)

        binding.imageView.animation = topAnimation
        binding.appName.animation = leftAnimation
        binding.cotes.animation = rightAnimation
        binding.devName.animation = bottomAnimation

        Handler(Looper.getMainLooper()).postDelayed({
//            checkForExistence()
            if(isLogin == "NONE") {
                startActivity(Intent(this@SplashScreen, StartActivity::class.java))
                finish()
            } else if(isLogin == USER_NODE){
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            } else if(isLogin == ADMIN_USER_NODE) {
                startActivity(Intent(this@SplashScreen, AdminMainActivity::class.java))
                finish()
            }
        }, 3000)
    }

}