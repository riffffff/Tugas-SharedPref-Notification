package com.example.mytugas_api.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_dogs") // Tabel Room untuk data favorit
data class DogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary Key dengan auto increment
    @SerializedName("message") val picture: String, // URL gambar
    @SerializedName("status") val status: String // Status (contoh: "favorit")
)
