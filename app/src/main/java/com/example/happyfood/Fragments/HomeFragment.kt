package com.example.happyfood.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.happyfood.R
import com.example.happyfood.adapter.PopularAdapter
import com.example.happyfood.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.advertisement1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.advertisement2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.advertisement3, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.advertisement4, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
//        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object :ItemClickListener{
            override fun doubleClick(position: Int) { }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })

        val foodName = listOf("Burger", "Pizza", "Momo", "Chole Bhature")
        val foodPrice = listOf("₹70", "₹150", "₹40", "₹60")
        val foodPic = listOf(R.drawable.burger, R.drawable.pizza,
            R.drawable.momos, R.drawable.chole_bhature)

        val adapter = PopularAdapter(foodName, foodPrice, foodPic, requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter


        binding.foodMenu.setOnClickListener {
            findNavController().navigate(R.id.searchFragment
            )
        }
    }
    companion object {

    }
}