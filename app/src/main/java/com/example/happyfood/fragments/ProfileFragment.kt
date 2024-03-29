package com.example.happyfood.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.happyfood.LoginActivity
import com.example.happyfood.databinding.FragmentProfileBinding
import com.example.happyfood.model.SHARED_PREFERENCE_KEY
import com.example.happyfood.model.USER_NODE
import com.example.happyfood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding =  FragmentProfileBinding.inflate(inflater, container, false)

        binding.apply {
            name.isEnabled = false
            address.isEnabled = false
            email.isEnabled = false
            phoneNumber.isEnabled = false
        }

        var isEnable = false
        binding.emailEdt.setOnClickListener {
            Toast.makeText(context,"You can't edit email",Toast.LENGTH_SHORT).show()
        }
        binding.editProfile.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.phoneNumber.isEnabled = isEnable
            if (isEnable) {
                binding.name.requestFocus()
                Toast.makeText(context, "Editing Enabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        setUserData()

        binding.saveInfo.setOnClickListener {
            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phoneNumber.text.toString()

            updateUserData(name, email, address, phone)

        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val sharedPreferences = context?.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.remove(SHARED_PREFERENCE_KEY)
            editor?.apply()

            startActivity(Intent(context, LoginActivity::class.java))
            requireActivity().finish()
        }

        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference(USER_NODE).child(userId)
            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "address" to address,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            } .addOnFailureListener {
                Toast.makeText(context, "Failed to Update Profile", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = database.getReference(USER_NODE).child(userId)
            userReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if( userProfile != null) {
                            binding.name.setText(userProfile.name)
                            binding.email.setText(userProfile.email)
                            binding.address.setText(userProfile.address)
                            binding.phoneNumber.setText(userProfile.phone)

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }


}