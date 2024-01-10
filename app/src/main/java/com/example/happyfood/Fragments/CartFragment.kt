package com.example.happyfood.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.PaymentActivity
import com.example.happyfood.R
import com.example.happyfood.adapter.CartAdapter
import com.example.happyfood.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val foodName = listOf("Burger", "Pizza", "Momo", "Chole Bhature")
        val foodPrice = listOf("₹70", "₹150", "₹40", "₹60")
        val foodPic = listOf(R.drawable.burger, R.drawable.pizza,
            R.drawable.momos, R.drawable.chole_bhature)

        val adapter = CartAdapter(ArrayList(foodName), ArrayList(foodPrice), ArrayList(foodPic))
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        binding.gotoPayment.setOnClickListener {
            startActivity(Intent(requireContext(), PaymentActivity::class.java))
        }

        return binding.root
    }

    companion object {

    }
}