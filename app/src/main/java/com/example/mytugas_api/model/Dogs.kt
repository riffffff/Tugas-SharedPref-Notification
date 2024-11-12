package com.example.mytugas_api.model

import com.google.gson.annotations.SerializedName

data class Dogs(

    @SerializedName("message")
    val picture: String,

    @SerializedName("status")
    val status: String,
)
//ini untuk mendefinisikan apasaja yang akan diambil