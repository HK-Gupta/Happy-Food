package com.example.happyfood.model

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserModel (
    val email: String? = null,
    val password: String? = null,
    val name: String? = null,
    val address: String? = null,
    val phone: String? = null
)