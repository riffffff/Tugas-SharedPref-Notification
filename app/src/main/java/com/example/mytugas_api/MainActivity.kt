package com.example.mytugas_api

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mytugas_api.databinding.ActivityMainBinding
import com.example.mytugas_api.model.Dogs
import com.example.mytugas_api.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val dogList = ArrayList<String>() // Inisialisasi daftar untuk menyimpan URL gambar anjing
    private lateinit var adapter: DataAdapter // Deklarasi adapter untuk RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView() // Panggil fungsi untuk menyiapkan RecyclerView
        fetchDogImages(9) // Panggil fungsi untuk mengambil 9 gambar anjing
    }

    private fun setupRecyclerView() {
        adapter = DataAdapter(dogList) { imageUrl ->
            // Tampilkan Toast ketika item di klik
            Toast.makeText(this@MainActivity, "Anda mengklik: $imageUrl", Toast.LENGTH_SHORT).show()
        }

        binding.rvData.apply {
            adapter = this@MainActivity.adapter
            layoutManager = GridLayoutManager(this@MainActivity, 3) // Atur layout menjadi Grid dengan 3 kolom
            setHasFixedSize(true) // Optimalkan ukuran RecyclerView
        }
    }

    private fun fetchDogImages(count: Int) {
        val client = ApiClient.getInstance()
        repeat(count) {
            client.getAllDogs().enqueue(object : Callback<Dogs> {
                override fun onResponse(call: Call<Dogs>, response: Response<Dogs>) {
                    if (response.isSuccessful && response.body() != null) {
                        val dogs = response.body()
                        dogs?.picture?.let {
                            dogList.add(it)
                            adapter.notifyItemInserted(dogList.size - 1) // Beritahu adapter ada item baru
                        }
                    } else {
                        Log.e("MainActivity", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Dogs>, t: Throwable) {
                    Log.e("MainActivity", "Network request failed: ${t.message}")
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
