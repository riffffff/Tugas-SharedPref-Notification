package com.example.mytugas_api

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytugas_api.databinding.ItemPictureBinding

// Typealias untuk fungsi callback
typealias OnClickData = (String) -> Unit
typealias OnFavoriteClick = (String) -> Unit

class DataAdapter(
    private var listDogs: List<String>, // Daftar URL gambar anjing
    private val onClickData: OnClickData, // Callback untuk klik data
    private val onFavoriteClick: OnFavoriteClick // Callback untuk klik favorit
) : RecyclerView.Adapter<DataAdapter.ItemDataViewHolder>() {

    // ViewHolder untuk item data
    inner class ItemDataViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            with(binding) {
                // Memuat gambar menggunakan Glide
                Glide.with(itemView.context).load(imageUrl).into(imgView)

                // Callback untuk klik gambar
                imgView.setOnClickListener {
                    onClickData(imageUrl)
                }

                // Callback untuk klik tombol favorit
                favoriteButton.setOnClickListener {
                    onFavoriteClick(imageUrl)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataViewHolder {
        val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemDataViewHolder(binding) // Mengembalikan ViewHolder
    }

    override fun getItemCount(): Int = listDogs.size // Menghitung jumlah item

    override fun onBindViewHolder(holder: ItemDataViewHolder, position: Int) {
        holder.bind(listDogs[position]) // Mengikat data ke ViewHolder
    }
}
