package com.example.happyfood.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.adapter.AdminMenuItemAdapter
import com.example.happyfood.databinding.ActivityAllItemBinding
import com.example.happyfood.model.AllMenu
import com.example.happyfood.model.MENU_NODE
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<AllMenu> = ArrayList()

    private val binding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        retrieveMenuItems()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodReference: DatabaseReference = database.reference.child(MENU_NODE)

        foodReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for (food in snapshot.children) {
                    val item = food.getValue(AllMenu::class.java)
                    item?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error: ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        val adapter = AdminMenuItemAdapter(this, menuItems) {
            deleteMenuItems(it)
        }
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.itemRecyclerView.adapter = adapter
    }

    private fun deleteMenuItems(position: Int) {
        val menuItemToDelete = menuItems[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuReference = database.reference.child(MENU_NODE).child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener {
            if(it.isSuccessful) {
                menuItems.removeAt(position)
                binding.itemRecyclerView.adapter?.notifyItemRemoved(position)
            } else {
                Toast.makeText(this, "Failed To Delete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}