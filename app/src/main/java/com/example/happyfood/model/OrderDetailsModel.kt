package com.example.happyfood.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetailsModel(): Serializable {
    var userUid: String? = null
    var userName: String? = null
    var foodNames: MutableList<String>? = null
    var foodPrice: MutableList<String>? = null
    var foodImage: MutableList<String>? = null
    var foodQuantities: MutableList<Int>? = null
    var address: String? = null
    var totalPrice: String? = null
    var phoneNumber: String? = null
    var orderAccept: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String? = null
    var currentTime: Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccept = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }
    constructor(
        userId: String,
        name: String,
        buyFoodName: ArrayList<String>,
        buyFoodPrice: ArrayList<String>,
        buyFoodImage: ArrayList<String>,
        buyFoodQuantity: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phone: String,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        time: Long
    ) : this() {
        this.userUid = userId
        this.userName = name
        this.foodNames = buyFoodName
        this.foodPrice = buyFoodPrice
        this.foodImage = buyFoodImage
        this.foodQuantities = buyFoodQuantity
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.itemPushKey = itemPushKey
        this.currentTime = time
        this.orderAccept = b
        this.paymentReceived = b1
    }

    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccept) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetailsModel> {
        override fun createFromParcel(parcel: Parcel): OrderDetailsModel {
            return OrderDetailsModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetailsModel?> {
            return arrayOfNulls(size)
        }
    }


}