package com.example.mytugas_api

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytugas_api.databinding.ItemPictureBinding

typealias OnClickData = (String) -> Unit

class DataAdapter(
    private var listDogs: List<String>, // Daftar URL gambar anjing
    private val OnClickData: OnClickData // Fungsi klik untuk item
) : RecyclerView.Adapter<DataAdapter.ItemDataViewHolder>() {

    inner class ItemDataViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            with(binding) {
                Glide.with(itemView.context).load(imageUrl).into(imgView) // Memuat gambar dengan Glide
                itemView.setOnClickListener {
                    OnClickData(imageUrl) // Panggil fungsi klik dengan URL gambar
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataViewHolder {
        val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemDataViewHolder(binding)
    }

    override fun getItemCount(): Int = listDogs.size

    override fun onBindViewHolder(holder: ItemDataViewHolder, position: Int) {
        holder.bind(listDogs[position]) // Mengikat URL gambar ke ViewHolder
    }
}
