package com.example.happyfood.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.adapter.AdminDeliveryAdapter
import com.example.happyfood.databinding.ActivityDeliveryStatusBinding
import com.example.happyfood.model.COMPLETED_ORDER
import com.example.happyfood.model.OrderDetailsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeliveryStatusActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrders: ArrayList<OrderDetailsModel> = arrayListOf()

    private val binding by lazy {
        ActivityDeliveryStatusBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        retrieveCompleteOrderDetails()

        binding.btnBack.setOnClickListener { finish() }

    }

    private fun retrieveCompleteOrderDetails() {
        database = FirebaseDatabase.getInstance()
        val completeOrderReference = database.reference.child(COMPLETED_ORDER).orderByChild("currentTime")

        completeOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrders.clear()

                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrderDetailsModel::class.java)
                    completeOrder?.let {
                        listOfCompleteOrders.add(it)
                    }
                }

                listOfCompleteOrders.reverse() // For Displaying in latest order

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setDataIntoRecyclerView() {
        val customerName = mutableListOf<String>()
        val paymentReceived = mutableListOf<Boolean>()

        for (details in listOfCompleteOrders) {
            details.userName?.let {
                customerName.add(it)
            }
            paymentReceived.add(details.paymentReceived)
        }

        val adapter = AdminDeliveryAdapter(customerName, paymentReceived)
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deliveryRecyclerView.adapter = adapter
    }
}