package com.example.mytugas_api.model

import com.google.gson.annotations.SerializedName

// Data class untuk merepresentasikan data JSON dari API
data class Dogs(

    @SerializedName("message")
    val picture: String, // URL gambar anjing yang diterima dari API

    @SerializedName("status")
    val status: String, // Status respons (contoh: "success")
)
