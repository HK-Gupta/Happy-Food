package com.example.happyfood.admin

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.happyfood.R
import com.example.happyfood.databinding.ActivityAddItemBinding
import com.example.happyfood.model.AllMenu
import com.example.happyfood.model.MENU_IMAGE
import com.example.happyfood.model.MENU_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    // Food Item Variables
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredients: String
    private var foodImageUri: Uri? = null

    // Firebase Variables
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Firebase auth Initialisation.
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.addItem.setOnClickListener {
            // Getting the data value from the edittext
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.descriptionText.text.toString().trim()
            foodIngredients = binding.ingredientText.text.toString().trim()

            if (foodName.isEmpty() || foodPrice.isEmpty() || foodDescription.isEmpty() || foodIngredients.isEmpty()) {
                Toast.makeText(this, "Kindly Fill All the Details", Toast.LENGTH_SHORT).show()
            } else {
                uploadData()
            }
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {

        // Creating a reference to the "Menu" node in database.
        val menuRef = database.getReference(MENU_NODE)
        // Generating a unique id for each menu item.
        val itemId = menuRef.push().key

        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child(MENU_IMAGE+"/${itemId}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl->
                    val newItem = AllMenu(foodName, foodPrice, foodDescription,
                                            foodIngredients, downloadUrl.toString())
                    itemId?.let { key->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        } .addOnFailureListener {
                            Toast.makeText(this, "Failed To Upload!", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            } .addOnFailureListener {
                Toast.makeText(this, "Failed To Upload!", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
        }

    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri->
        if(uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }

}