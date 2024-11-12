package com.example.mytugas_api.network

import com.example.mytugas_api.model.Dogs
import retrofit2.Call
import retrofit2.http.GET

// Interface yang mendefinisikan endpoint untuk mengakses API
interface ApiService {

    // Mendefinisikan metode untuk mengambil gambar acak dari API
    @GET("breeds/image/random")
    fun getAllDogs(): Call<Dogs> // Mengembalikan objek Call yang berisi data dari model Dogs
}
