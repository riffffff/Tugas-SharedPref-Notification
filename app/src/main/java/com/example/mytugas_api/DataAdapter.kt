package com.example.mytugas_api

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytugas_api.databinding.ItemPictureBinding

typealias OnClickData = (String) -> Unit
typealias OnFavoriteClick = (String) -> Unit

class DataAdapter(
    private var listDogs: List<String>,
    private val onClickData: OnClickData,
    private val onFavoriteClick: OnFavoriteClick
) : RecyclerView.Adapter<DataAdapter.ItemDataViewHolder>() {

    inner class ItemDataViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            with(binding) {
                Glide.with(itemView.context).load(imageUrl).into(imgView)
                imgView.setOnClickListener {
                    onClickData(imageUrl)
                }
                favoriteButton.setOnClickListener {
                    onFavoriteClick(imageUrl)
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
        holder.bind(listDogs[position])
    }
}
