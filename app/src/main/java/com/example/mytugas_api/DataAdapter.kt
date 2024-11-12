package com.example.mytugas_api

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytugas_api.databinding.ItemPictureBinding

// Mendefinisikan typealias untuk fungsi yang akan dijalankan saat item di klik
typealias OnClickData = (String) -> Unit

class DataAdapter(
    private var listDogs: List<String>, // List berisi URL gambar anjing
    private val OnClickData: OnClickData // Fungsi yang akan dipanggil saat item diklik
) : RecyclerView.Adapter<DataAdapter.ItemDataViewHolder>() {

    // ViewHolder internal untuk menangani setiap item dalam RecyclerView
    inner class ItemDataViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Fungsi untuk mengikat data ke tampilan item
        fun bind(imageUrl: String) {
            with(binding) {
                // Memuat gambar dari URL ke dalam ImageView menggunakan Glide
                Glide.with(itemView.context).load(imageUrl).into(imgView)

                // Menetapkan fungsi klik pada item untuk memanggil OnClickData
                itemView.setOnClickListener {
                    OnClickData(imageUrl) // Mengirimkan URL gambar yang diklik ke fungsi
                }
            }
        }
    }

    // Membuat ViewHolder baru ketika RecyclerView membutuhkan item baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataViewHolder {
        // Meng-inflate layout item dari ItemPictureBinding
        val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemDataViewHolder(binding) // Mengembalikan instance ItemDataViewHolder
    }

    // Mengembalikan jumlah item yang ada dalam list
    override fun getItemCount(): Int {
        return listDogs.size
    }

    // Mengikat data dari list ke ViewHolder berdasarkan posisi item
    override fun onBindViewHolder(holder: ItemDataViewHolder, position: Int) {
        holder.bind(listDogs[position]) // Mengikat URL gambar ke ViewHolder pada posisi tertentu
    }
}
