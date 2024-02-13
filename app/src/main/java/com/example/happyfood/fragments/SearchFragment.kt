package com.example.happyfood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.adapter.PopularAdapter
import com.example.happyfood.databinding.FragmentSearchBinding
import com.example.happyfood.model.MENU_NODE
import com.example.happyfood.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: PopularAdapter
    private lateinit var totalMenuItems: MutableList<MenuItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        retrieveMenuItemFromDatabase()

        // Setup for Search View
        setupSearchView()


        return binding.root
    }

    private fun retrieveMenuItemFromDatabase() {
        database = FirebaseDatabase.getInstance()
        totalMenuItems = mutableListOf()
        val foodReference = database.reference.child(MENU_NODE)
        foodReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        totalMenuItems.add(it)
                    }
                }

                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showAllMenu() {
        val filteredItems = ArrayList(totalMenuItems)
        setAdapter(filteredItems)
    }

    private fun setAdapter(filteredItems: List<MenuItem>) {
        adapter = PopularAdapter(filteredItems, requireContext())
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerView.adapter = adapter
    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterSearchItem(query)
                return true
            }
            override fun onQueryTextChange(text: String): Boolean {
                filterSearchItem(text)
                return true
            }
        })
    }

    private fun filterSearchItem(query: String) {
        val filteredItems = totalMenuItems.filter {
            it.foodName?.contains(query, ignoreCase = true) == true
        }
        setAdapter(filteredItems)
    }

}