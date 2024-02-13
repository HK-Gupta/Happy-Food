package com.example.happyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.happyfood.fragments.BottomNavigationFragment
import com.example.happyfood.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navController: NavController = findNavController(R.id.fragmentContainerView)

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bell.setOnClickListener {
            val bottomSheetDialog = BottomNavigationFragment()
            bottomSheetDialog.show(supportFragmentManager, "Test")
        }


    }
}