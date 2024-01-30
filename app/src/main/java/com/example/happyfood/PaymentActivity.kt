package com.example.happyfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.happyfood.fragments.CongratsBottomSheet
import com.example.happyfood.databinding.ActivityPaymentBinding
import com.example.happyfood.model.CART_ITEMS
import com.example.happyfood.model.ORDER_DETAILS
import com.example.happyfood.model.ORDER_HISTORY
import com.example.happyfood.model.OrderDetailsModel
import com.example.happyfood.model.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PaymentActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var buyFoodName: ArrayList<String>
    private lateinit var buyFoodPrice: ArrayList<String>
    private lateinit var buyFoodImage: ArrayList<String>
    private lateinit var buyFoodQuantity: ArrayList<Int>



    private val binding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        setUserData()

        buyFoodName = intent.getStringArrayListExtra("BuyFoodName") as ArrayList<String>
        buyFoodPrice = intent.getStringArrayListExtra("BuyFoodPrice") as ArrayList<String>
        buyFoodImage = intent.getStringArrayListExtra("BuyFoodImage") as ArrayList<String>
        buyFoodQuantity = intent.getIntegerArrayListExtra("BuyFoodQuantity") as ArrayList<Int>


        totalAmount = calculateTotalAmmount().toString()
        binding.totalAmount.text = "₹ $totalAmount"

        binding.orderPlaced.setOnClickListener {
            name = binding.txtName.text.toString()
            address = binding.txtAddress.text.toString()
            phone = binding.phoneNumber.text.toString()

            if(name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please Update Your profile", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itemPushKey = database.child(ORDER_DETAILS).push().key
        val orderDetails = OrderDetailsModel(userId, name, buyFoodName, buyFoodPrice,
            buyFoodImage, buyFoodQuantity, address, totalAmount, phone,
            false, false, itemPushKey, time)
        val orderReference = database.child(ORDER_DETAILS).child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            CongratsBottomSheet().show(supportFragmentManager, "Test")
            removeItemFromCart()
            addOrderToHistory(orderDetails)
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetailsModel) {
        database.child(USER_NODE).child(userId).child(ORDER_HISTORY)
            .child(orderDetails.itemPushKey!!).setValue(orderDetails)
            .addOnSuccessListener {

            } .addOnFailureListener {
                Toast.makeText(this, "Failed To Order", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeItemFromCart() {
        val cartItemReference = database.child(USER_NODE).child(userId).child(CART_ITEMS)
        cartItemReference.removeValue()
    }

    private fun calculateTotalAmmount(): Int {
        var amount = 0;

        for(i in 0 until buyFoodPrice.size) {
            val price = buyFoodPrice[i]
            val lastChar = price.last()
            val priceValue = if(lastChar == R.string.rupees.toChar()) {
                price.dropLast(1).toInt()
            } else {
                price.toInt()
            }

            var quantity = buyFoodQuantity[i]
            amount += priceValue*quantity
        }

        return amount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if(user != null) {
            val userId: String = user.uid
            val userReference = database.child(USER_NODE).child(userId)

            userReference.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java)?:""
                        val address = snapshot.child("address").getValue(String::class.java)?:""
                        val phone = snapshot.child("phone").getValue(String::class.java)?:""

                        binding.apply {
                            txtName.setText(name)
                            txtAddress.setText(address)
                            phoneNumber.setText(phone)

                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}