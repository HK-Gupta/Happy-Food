package com.example.happyfood.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.R
import com.example.happyfood.adapter.HistoryAdapter
import com.example.happyfood.databinding.FragmentHistoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()

        return binding.root
    }

    companion object {

    }

    private fun setupRecyclerView() {
        val foodName = listOf("Burger", "Pizza", "Momo", "Chole Bhature, Burger", "Pizza", "Momo", "Chole Bhature")
        val foodPrice = listOf("₹70", "₹150", "₹40", "₹60", "₹70", "₹150", "₹40", "₹60")
        val foodPic = listOf(R.drawable.burger, R.drawable.pizza, R.drawable.momos, R.drawable.chole_bhature,
            R.drawable.burger, R.drawable.pizza, R.drawable.momos, R.drawable.chole_bhature)

        adapter = HistoryAdapter(foodName, foodPrice, foodPic)
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = adapter
    }
}