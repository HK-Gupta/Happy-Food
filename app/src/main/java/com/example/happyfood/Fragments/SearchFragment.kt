package com.example.happyfood.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.R
import com.example.happyfood.adapter.CartAdapter
import com.example.happyfood.adapter.SearchAdapter
import com.example.happyfood.databinding.FragmentSearchBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val foodName = listOf("Burger", "Pizza", "Momo", "Chole Bhature")
    private val foodPrice = listOf("₹70", "₹150", "₹40", "₹60")
    private val foodPic = listOf(R.drawable.burger, R.drawable.pizza,
        R.drawable.momos, R.drawable.chole_bhature)

    private lateinit var adapter: SearchAdapter

    private val filteredFoodName = mutableListOf<String>()
    private val filteredFoodPrice = mutableListOf<String>()
    private val filteredFoodPic = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        adapter = SearchAdapter(filteredFoodName, filteredFoodPrice, filteredFoodPic, requireContext())
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerView.adapter = adapter


        // Setup for Search View
        setupSearchView()

        // Show all Items
        showAllItems()

        return binding.root
    }

    private fun showAllItems() {
        filteredFoodName.clear()
        filteredFoodPrice.clear()
        filteredFoodPic.clear()

        filteredFoodName.addAll(foodName)
        filteredFoodPrice.addAll(foodPrice)
        filteredFoodPic.addAll(foodPic)

        adapter.notifyDataSetChanged()
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
        filteredFoodName.clear()
        filteredFoodPrice.clear()
        filteredFoodPic.clear()

        foodName.forEachIndexed { index, name ->
            if(name.contains(query, ignoreCase = true)) {
                filteredFoodName.add((name))
                filteredFoodPrice.add(foodPrice[index])
                filteredFoodPic.add(foodPic[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}