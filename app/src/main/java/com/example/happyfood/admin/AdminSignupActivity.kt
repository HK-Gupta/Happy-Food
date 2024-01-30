package com.example.happyfood.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.happyfood.model.ADMIN_USER_NODE
import com.example.happyfood.model.AdminUserModel
import com.example.happyfood.databinding.ActivityAdminSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AdminSignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var restaurantName: String

    private val binding by lazy {
        ActivityAdminSignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialising Auth and Database
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.createUser.setOnClickListener {
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()
            userName = binding.name.text.toString().trim()
            restaurantName = binding.restaurantName.text.toString().trim()

            if(email.isEmpty() || password.isEmpty() || userName.isEmpty() || restaurantName.isEmpty()) {
                Toast.makeText(this, "Kindly Fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password, userName, restaurantName)
            }
        }

        val locationList = arrayOf("Delhi", "Mumbai", "Kolkata", "Chennai");
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        binding.listOfLocation.setAdapter(adapter)

        binding.gotoLogin.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
            finish()
        }
    }

    private fun createAccount(email: String, password: String, userName: String, restaurantName: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully.", Toast.LENGTH_SHORT).show()
                saveUserData(email, password, userName, restaurantName)
                startActivity(Intent(this, AdminLoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Something went wrong \nPlease Try Again", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveUserData(email: String, password: String, userName: String, restaurantName: String) {
        val user = AdminUserModel(email, password, userName, restaurantName)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(ADMIN_USER_NODE).child(userId).setValue(user)
    }
}