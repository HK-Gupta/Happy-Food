package com.example.happyfood.model

data class CartModel (
    var foodName: String? = null,
    var foodPrice: String? = null,
    var foodImage: String? = null,
    var foodQuantity: Int? = 1
)
