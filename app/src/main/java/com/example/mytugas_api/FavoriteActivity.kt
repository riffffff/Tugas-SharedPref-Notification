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
    private val binding by lazy { ActivityFavoriteBinding.inflate(layoutInflater) }
    private lateinit var db: AppDatabase
    private val favoriteList = ArrayList<DogEntity>()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "dog-database").build()

        setupRecyclerView()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        adapter = FavoriteAdapter(favoriteList, onDeleteClick = { dog ->
            deleteFromFavorites(dog)
        })
        binding.rvFavorites.apply {
            adapter = this@FavoriteActivity.adapter
            layoutManager = GridLayoutManager(this@FavoriteActivity, 2) // Mengatur spanCount menjadi 3
            setHasFixedSize(true)
        }
    }

    private fun loadFavorites() {
        Thread {
            val favorites = db.dogDao().getAll()
            Log.d("FavoriteActivity", "Favorites loaded: ${favorites.size}")
            favorites.forEach { favorite ->
                Log.d("FavoriteActivity", "Favorite: ${favorite.picture}")
            }

            if (favorites.isNotEmpty()) {
                favoriteList.clear()
                favoriteList.addAll(favorites)
                runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            } else {
                Log.d("FavoriteActivity", "No favorites found")
            }
        }.start()
    }

    private fun deleteFromFavorites(dog: DogEntity) {
        Thread {
            db.dogDao().deleteById(dog.id)
            runOnUiThread {
                favoriteList.remove(dog)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Gambar dihapus dari favorit", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}
