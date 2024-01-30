package com.example.happyfood.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyfood.R
import com.example.happyfood.SignupActivity
import com.example.happyfood.StartActivity
import com.example.happyfood.databinding.ActivityAdminMainBinding
import com.example.happyfood.model.COMPLETED_ORDER
import com.example.happyfood.model.ORDER_DETAILS
import com.example.happyfood.model.OrderDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminMainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    private  val binding by lazy {
        ActivityAdminMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        binding.pendingOrders.setOnClickListener {
            startActivity(Intent(this, PendingOrderActivity::class.java))

        }
        binding.addItem.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
        binding.seeAllItem.setOnClickListener {
            startActivity(Intent(this, AllItemActivity::class.java))
        }
        binding.deliveryStatus.setOnClickListener {
            startActivity(Intent(this, DeliveryStatusActivity::class.java))
        }
        binding.gotoProfile.setOnClickListener {
            startActivity(Intent(this, AdminProfileActivity::class.java))
        }
        binding.createUser.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        binding.logout.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }

        pendingOrders()
        completedOrders()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        val completedOrderReference = database.reference.child(COMPLETED_ORDER)
        var listOfTotalPay = mutableListOf<Int>()

        completedOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for( foodSnapshot in snapshot.children ) {
                    var totalOrders = foodSnapshot.getValue(OrderDetailsModel::class.java)
                    totalOrders?.totalPrice?.replace("₹", "")?.toIntOrNull()?.let {
                        listOfTotalPay.add(it)
                    }
                }

                binding.totalEarning.text = "₹ ${listOfTotalPay.sum()}"
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun completedOrders() {
        val completeOrderReference = database.reference.child(COMPLETED_ORDER)
        var completedOrdersCount = 0

        completeOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrdersCount = snapshot.childrenCount.toInt()
                binding.completeOrderCount.text = completedOrdersCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun pendingOrders() {
        val pendingOrderReference = database.reference.child(ORDER_DETAILS)
        var pendingOrdersCount = 0

        pendingOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrdersCount = snapshot.childrenCount.toInt()
                binding.pendingOrderCount.text = pendingOrdersCount.toString()
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}