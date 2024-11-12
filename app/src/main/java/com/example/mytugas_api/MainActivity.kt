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
    // Menggunakan lazy initialization untuk menghubungkan layout binding
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val dogList = ArrayList<String>() // Menginisialisasi ArrayList untuk menyimpan URL gambar anjing
    private lateinit var adapter: DataAdapter // Adapter khusus untuk menampilkan data dalam RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) // Menetapkan tampilan dari layout yang di-inflate oleh binding

        setupRecyclerView() // Mengatur RecyclerView agar dapat menampilkan data
        fetchDogImages(9) // Memanggil metode untuk mengambil 9 gambar anjing secara acak
    }

    private fun setupRecyclerView() {
        // Inisialisasi adapter dengan data dari dogList dan menentukan aksi saat item diklik
        adapter = DataAdapter(dogList) { imageUrl ->
            // Menampilkan Toast ketika item di klik, menunjukkan URL gambar
            Toast.makeText(this@MainActivity, "Anda mengklik: $imageUrl", Toast.LENGTH_SHORT).show()
        }

        // Mengatur RecyclerView untuk menampilkan data dengan tata letak grid 3 kolom
        binding.rvData.apply {
            adapter = this@MainActivity.adapter
            layoutManager = GridLayoutManager(this@MainActivity, 3)
        }
    }

    private fun fetchDogImages(count: Int) {
        // Mendapatkan instance dari ApiClient
        val client = ApiClient.getInstance()

        // Mengulangi proses pengambilan data sebanyak jumlah yang ditentukan
        repeat(count) {
            client.getAllDogs().enqueue(object : Callback<Dogs> {
                // Callback ketika respons API berhasil
                override fun onResponse(call: Call<Dogs>, response: Response<Dogs>) {
                    if (response.isSuccessful && response.body() != null) {
                        // Menyimpan URL gambar ke dalam list dan memberi tahu adapter agar memperbarui tampilan
                        val dogs = response.body()
                        dogs?.picture?.let {
                            dogList.add(it)
                            adapter.notifyItemInserted(dogList.size - 1) // Memberi tahu adapter ada item baru
                        }
                    } else {
                        // Log jika respons gagal atau kosong, menampilkan pesan Toast untuk pengguna
                        Log.e("MainActivity", "Response failed: ${response.errorBody()?.string()}")
                        Toast.makeText(this@MainActivity, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                    }
                }

                // Callback ketika ada kegagalan dalam permintaan jaringan
                override fun onFailure(call: Call<Dogs>, t: Throwable) {
                    // Log error dan menampilkan pesan Toast untuk menunjukkan kesalahan jaringan
                    Log.e("MainActivity", "Network request failed: ${t.message}")
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
