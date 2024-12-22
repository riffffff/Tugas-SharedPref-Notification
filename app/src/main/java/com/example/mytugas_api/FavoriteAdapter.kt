package com.example.mytugas_api

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytugas_api.databinding.ItemFavoriteBinding
import com.example.mytugas_api.model.DogEntity

class FavoriteAdapter(
    private var listFavorites: List<DogEntity>, // Daftar data favorit
    private val onDeleteClick: (DogEntity) -> Unit // Fungsi callback untuk menghapus favorit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    // ViewHolder untuk item favorit
    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dog: DogEntity) {
            with(binding) {
                // Memuat gambar menggunakan Glide
                Glide.with(itemView.context).load(dog.picture).into(imgView)
                deleteFavoriteButton.text = "Delete" // Mengatur teks tombol
                deleteFavoriteButton.setOnClickListener {
                    onDeleteClick(dog) // Memanggil fungsi callback saat tombol diklik
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding) // Mengembalikan ViewHolder
    }

    override fun getItemCount(): Int = listFavorites.size // Menghitung jumlah item

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position]) // Mengikat data ke ViewHolder
    }
}
