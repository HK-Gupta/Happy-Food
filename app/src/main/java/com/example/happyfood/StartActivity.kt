package com.example.happyfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.happyfood.admin.AdminLoginActivity
import com.example.happyfood.admin.AdminMainActivity
import com.example.happyfood.databinding.ActivityStartBinding
import com.example.happyfood.model.ADMIN_USER_NODE
import com.example.happyfood.model.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.buttonUser.setOnClickListener {
            checkUserExist(USER_NODE, object : UserExistCallback {
                override fun onUserExist(exists: Boolean) {
                    if (exists) {
                        startActivity(Intent(this@StartActivity, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@StartActivity, LoginActivity::class.java))
                    }
                }
            })
        }
        binding.buttonAdmin.setOnClickListener {
            checkUserExist(ADMIN_USER_NODE, object : UserExistCallback {
                override fun onUserExist(exists: Boolean) {
                    if (exists) {
                        startActivity(Intent(this@StartActivity, AdminMainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@StartActivity, AdminLoginActivity::class.java))
                    }
                }
            })
        }
    }

    private fun checkUserExist(userNode: String, callback: UserExistCallback) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val databaseReference = database.reference.child(userNode).child(uid)

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback.onUserExist(snapshot.exists())
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onUserExist(false)
                }
            })
        } else {
            callback.onUserExist(false)
        }
    }
}
interface UserExistCallback {
    fun onUserExist(exists: Boolean)
}

