package com.example.happyfood.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.happyfood.RecentOrderItems
import com.example.happyfood.adapter.HistoryAdapter
import com.example.happyfood.databinding.FragmentHistoryBinding
import com.example.happyfood.model.COMPLETED_ORDER
import com.example.happyfood.model.ORDER_HISTORY
import com.example.happyfood.model.OrderDetailsModel
import com.example.happyfood.model.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: ArrayList<OrderDetailsModel> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        retrieveItemBuyHistory()

        binding.recentHistory.setOnClickListener {
            seeRecentBuyItems()
        }

        binding.receivedButton.setOnClickListener {
            updateOrderStatus()
            Toast.makeText(context, "Thanks for Shopping your Order will be delivered within few minutes", Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child(COMPLETED_ORDER).child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)

    }

    private fun seeRecentBuyItems() {
        listOfOrderItem.firstOrNull()?.let {
            val intent = Intent(requireContext(), RecentOrderItems::class.java)
            intent.putExtra("RecentBuyItems", listOfOrderItem)
            startActivity(intent)
        }
    }

    private fun retrieveItemBuyHistory() {
        binding.recentHistory.visibility = View.GONE
        userId = auth.currentUser?.uid?: ""

        val buyItemReference = database.reference.child(USER_NODE).child(userId).child(ORDER_HISTORY)
        val sortingQuery: Query = buyItemReference.orderByChild("currentTime")

        sortingQuery.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetailsModel::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()

                if (listOfOrderItem.isNotEmpty()) {
                    setDataINRecentCardView()
                    setPreviousBuyItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setDataINRecentCardView() {
        binding.recentHistory.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItem.firstOrNull()
        recentOrderItem?.let {
            binding.apply {
                recentDishName.text = it.foodNames?.firstOrNull()?:""
                recentDishPrice.text = it.foodPrice?.firstOrNull()?:""
                val image = it.foodImage?.firstOrNull()?:""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(recentDishImage)

                val isOrderAccepted = listOfOrderItem[0].orderAccept
                if( isOrderAccepted ) {
                    orderStatus.background.setTint(Color.GREEN)
                    receivedButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setPreviousBuyItemsRecyclerView() {
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()

        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodNames?.firstOrNull()?.let { foodName.add(it) }
            listOfOrderItem[i].foodPrice?.firstOrNull()?.let { foodPrice.add(it) }
            listOfOrderItem[i].foodImage?.firstOrNull()?.let { foodImage.add(it) }
        }

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryAdapter(foodName, foodPrice, foodImage, requireContext())
        binding.historyRecyclerView.adapter = adapter
    }

}