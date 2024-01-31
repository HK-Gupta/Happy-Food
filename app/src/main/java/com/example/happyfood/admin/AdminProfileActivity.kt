package com.example.happyfood.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.happyfood.databinding.ActivityAdminProfileBinding
import com.example.happyfood.model.ADMIN_USER_NODE
import com.example.happyfood.model.AdminUserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    private val binding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.apply {
            name.isEnabled = false
            restaurantName.isEnabled = false
            address.isEnabled = false
            email.isEnabled = false
            number.isEnabled = false
            password.isEnabled = false
            btnSave.isEnabled = false
        }

        var isEnable = false
        binding.editProfile.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.restaurantName.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.number.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            binding.btnSave.isEnabled = isEnable
            if (isEnable) {
                binding.name.requestFocus()
                Toast.makeText(this@AdminProfileActivity, "Editing Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            adminReference = database.reference.child(ADMIN_USER_NODE).child(currentUserUid)
            retrieveUserData()
        }

        binding.btnSave.setOnClickListener {
            setUserData()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun retrieveUserData() {

        adminReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val ownerName = snapshot.child("userName").value
                    val restaurantName = snapshot.child("restaurantName").value
                    val email = snapshot.child("email").value
                    val address = snapshot.child("address").value
                    val phoneNumber = snapshot.child("phone").value
                    val password = snapshot.child("password").value

                    setDataToTextView(
                        ownerName,
                        restaurantName,
                        email,
                        address,
                        phoneNumber,
                        password
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setDataToTextView(
        ownerName: Any?,
        restaurant: Any?,
        emailId: Any?,
        resAddress: Any?,
        phone: Any?,
        passwordId: Any?
    ) {
        binding.apply {
            name.setText(ownerName.toString())
            restaurantName.setText(restaurant.toString())
            email.setText(emailId.toString())
            address.setText(resAddress.toString())
            number.setText(phone.toString())
            password.setText(passwordId.toString())


        }
    }

    private fun setUserData() {
        val updatedName = binding.name.text.toString()
        val updatedRestaurantName = binding.restaurantName.text.toString()
        val updatedEmail = binding.email.text.toString()
        val updatedAddress = binding.address.text.toString()
        val updatedPhoneNumber = binding.number.text.toString()
        val updatedPassword = binding.password.text.toString()

        val userData = AdminUserModel(
            updatedEmail,
            updatedPassword,
            updatedName,
            updatedRestaurantName,
            updatedAddress,
            updatedPhoneNumber
        )

        adminReference.setValue(userData).addOnSuccessListener {
            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            auth.currentUser?.verifyBeforeUpdateEmail(updatedEmail)
            auth.currentUser?.updatePassword(updatedPassword)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed To Updated!", Toast.LENGTH_SHORT).show()
        }
    }
}