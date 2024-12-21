package com.example.mytugas_api

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.mytugas_api.databinding.ActivityMainBinding
import com.example.mytugas_api.model.AppDatabase
import com.example.mytugas_api.model.Dogs
import com.example.mytugas_api.model.DogEntity
import com.example.mytugas_api.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dogList = ArrayList<String>()
    private lateinit var adapter: DataAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dog-database").build()

        setupRecyclerView()
        fetchDogImages(9)

        val btnFavorite: Button = findViewById(R.id.btn_favorite)
        btnFavorite.setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = DataAdapter(dogList, onClickData = { imageUrl ->
            Toast.makeText(this@MainActivity, "Anda mengklik: $imageUrl", Toast.LENGTH_SHORT).show()
        }, onFavoriteClick = { imageUrl ->
            saveToFavorites(imageUrl)
        })

        binding.rvData.apply {
            adapter = this@MainActivity.adapter
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            setHasFixedSize(true)
        }
    }

    private fun saveToFavorites(imageUrl: String) {
        Thread {
            val dogEntity = DogEntity(picture = imageUrl, status = "favorit")
            db.dogDao().insert(dogEntity)
            runOnUiThread {
                Toast.makeText(this, "Gambar ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
            }
        }.start()
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
                            adapter.notifyItemInserted(dogList.size - 1)
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
