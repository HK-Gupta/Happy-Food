package com.example.happyfood.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.adapter.PendingOrderAdapter
import com.example.happyfood.databinding.ActivityPendingOrderBinding
import com.example.happyfood.model.ACCEPTED_ORDER
import com.example.happyfood.model.COMPLETED_ORDER
import com.example.happyfood.model.ORDER_DETAILS
import com.example.happyfood.model.ORDER_HISTORY
import com.example.happyfood.model.OrderDetailsModel
import com.example.happyfood.model.USER_NODE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.FieldPosition

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {

    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfPrice: MutableList<String> = mutableListOf()
    private var listOfImages: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetailsModel> = arrayListOf()

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetailsReference: DatabaseReference

    private val binding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseOrderDetailsReference = database.reference.child(ORDER_DETAILS)

        getOrderDetails()

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun getOrderDetails() {
        databaseOrderDetailsReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetailsModel::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for(orderItem in listOfOrderItem) {
           // Add Data to respective List For Populating the View
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfPrice.add(it) }
            orderItem.foodImage?.filterNot { it.isEmpty() }?.forEach {
                listOfImages.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PendingOrderAdapter(this, listOfName, listOfPrice, listOfImages, this)
        binding.pendingOrderRecyclerView.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent =  Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        // Handling Item Acceptance ans Update DataBase
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child(ORDER_DETAILS).child(it)
        }
        clickItemOrderReference?.child(ACCEPTED_ORDER)?.setValue(true)

        updateOrderAcceptStatus(position)
    }

    private fun updateOrderAcceptStatus(position: Int) {
        // Taking the user id of the clicked item.
        val userIdClicked = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val buyHistoryReference = database.reference.child(USER_NODE)
            .child(userIdClicked!!).child(ORDER_HISTORY).child(pushKeyOfClickedItem!!)
        buyHistoryReference.child(ACCEPTED_ORDER).setValue(true)
        databaseOrderDetailsReference.child(pushKeyOfClickedItem).child(ACCEPTED_ORDER).setValue(true)
    }

    override fun onItemDispatchedClickListener(position: Int) {
        // Handling Item Acceptance ans Update DataBase
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference = database.reference
            .child(COMPLETED_ORDER).child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteItemFromOrderDetails(dispatchItemPushKey)
            }
    }

    private fun deleteItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderItemReference = database.reference.child(ORDER_DETAILS).child(dispatchItemPushKey)
        Log.d("ItemPushKey", dispatchItemPushKey)
        orderItemReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Order Dispatched Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show()
            }

        }
    }


}