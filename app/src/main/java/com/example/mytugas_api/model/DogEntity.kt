package com.example.mytugas_api.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_dogs")
data class DogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("message") val picture: String,
    @SerializedName("status") val status: String
)
