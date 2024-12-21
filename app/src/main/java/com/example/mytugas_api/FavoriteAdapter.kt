package com.example.mytugas_api

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytugas_api.databinding.ItemFavoriteBinding
import com.example.mytugas_api.model.DogEntity

class FavoriteAdapter(
    private var listFavorites: List<DogEntity>,
    private val onDeleteClick: (DogEntity) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dog: DogEntity) {
            with(binding) {
                Glide.with(itemView.context).load(dog.picture).into(imgView)
                deleteFavoriteButton.text = "Delete"
                deleteFavoriteButton.setOnClickListener {
                    onDeleteClick(dog)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavorites.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position])
    }
}
