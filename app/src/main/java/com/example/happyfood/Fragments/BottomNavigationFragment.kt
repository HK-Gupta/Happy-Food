package com.example.happyfood.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.happyfood.R
import com.example.happyfood.adapter.NotificationAdapter
import com.example.happyfood.databinding.FragmentBottonNavigationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomNavigationFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottonNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottonNavigationBinding.inflate(layoutInflater, container, false)

        val notification = listOf(getString(R.string.cancelled_delivery),
            getString(R.string.out_for_delivery), getString(R.string.delivered))
        val image = listOf(R.drawable.sad, R.drawable.delivery, R.drawable.checked)

        val adapter = NotificationAdapter(ArrayList(notification), ArrayList(image))
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter

        return binding.root
    }

    companion object {

    }
}