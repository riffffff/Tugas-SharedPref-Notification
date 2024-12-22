package com.example.mytugas_api

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.Room
import com.example.mytugas_api.databinding.ActivityFavoriteBinding
import com.example.mytugas_api.model.AppDatabase
import com.example.mytugas_api.model.DogEntity

class FavoriteActivity : AppCompatActivity() {
    private val binding by lazy { ActivityFavoriteBinding.inflate(layoutInflater) } // Binding layout
    private lateinit var db: AppDatabase // Inisialisasi database Room
    private val favoriteList = ArrayList<DogEntity>() // Daftar favorit
    private lateinit var adapter: FavoriteAdapter // Adapter RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Membuat database Room
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dog-database").build()

        setupRecyclerView() // Mengatur RecyclerView
        loadFavorites() // Memuat daftar favorit
    }

    private fun setupRecyclerView() {
        // Inisialisasi adapter dengan callback untuk menghapus data
        adapter = FavoriteAdapter(favoriteList, onDeleteClick = { dog ->
            deleteFromFavorites(dog) // Fungsi untuk menghapus data dari favorit
        })

        // Mengatur RecyclerView
        binding.rvFavorites.apply {
            adapter = this@FavoriteActivity.adapter
            layoutManager = GridLayoutManager(this@FavoriteActivity, 3) // Grid dengan 3 kolom
            setHasFixedSize(true)
        }
    }

    private fun loadFavorites() {
        // Memuat data dari database di dalam thread terpisah
        Thread {
            val favorites = db.dogDao().getAll() // Mendapatkan semua data favorit
            Log.d("FavoriteActivity", "Favorites loaded: ${favorites.size}")

            if (favorites.isNotEmpty()) {
                favoriteList.clear()
                favoriteList.addAll(favorites) // Menambahkan data ke daftar
                runOnUiThread {
                    adapter.notifyDataSetChanged() // Memberi tahu adapter untuk memperbarui tampilan
                }
            } else {
                Log.d("FavoriteActivity", "No favorites found")
            }
        }.start()
    }

    private fun deleteFromFavorites(dog: DogEntity) {
        // Menghapus data dari database di dalam thread terpisah
        Thread {
            db.dogDao().deleteById(dog.id) // Menghapus data berdasarkan ID
            runOnUiThread {
                favoriteList.remove(dog) // Menghapus data dari daftar
                adapter.notifyDataSetChanged() // Memperbarui tampilan
                Toast.makeText(this, "Gambar dihapus dari favorit", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}
