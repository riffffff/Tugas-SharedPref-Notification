package com.example.mytugas_api.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // Fungsi untuk mendapatkan instance dari ApiService
    fun getInstance() : ApiService {
        // Menambahkan interceptor untuk mencatat log HTTP
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)  // Menampilkan seluruh body dari request dan response

        // Membuat OkHttpClient dengan interceptor
        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor) // Menambahkan interceptor pada client
            .build()

        // Membuat Retrofit instance
        val builder = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")  // Menetapkan base URL untuk API
            .addConverterFactory(GsonConverterFactory.create())  // Menggunakan Gson untuk parsing JSON
            .client(mOkHttpClient)  // Menggunakan OkHttpClient yang telah dikonfigurasi
            .build()

        // Mengembalikan instance dari ApiService yang dapat digunakan untuk memanggil API
        return builder.create(ApiService::class.java)
    }
}
