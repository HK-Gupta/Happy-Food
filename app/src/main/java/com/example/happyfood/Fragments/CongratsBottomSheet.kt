package com.example.happyfood.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.happyfood.MainActivity
import com.example.happyfood.R
import com.example.happyfood.databinding.FragmentCongratsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCongratsBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCongratsBottomSheetBinding.inflate(inflater, container, false)
        binding.gotoHome.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        return binding.root
    }

    companion object {

    }
}