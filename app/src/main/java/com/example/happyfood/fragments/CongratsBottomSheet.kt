package com.example.happyfood.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.happyfood.MainActivity
import com.example.happyfood.databinding.FragmentCongratsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCongratsBottomSheetBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCongratsBottomSheetBinding.inflate(inflater, container, false)
        binding.gotoHome.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        return binding.root
    }


}