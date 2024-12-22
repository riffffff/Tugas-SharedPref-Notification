package com.example.mytugas_api.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DogDao {
    @Query("SELECT * FROM favorite_dogs")
    fun getAll(): List<DogEntity> // Mendapatkan semua data dari tabel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dog: DogEntity) // Menyimpan data ke tabel

    @Query("DELETE FROM favorite_dogs WHERE id = :id")
    fun deleteById(id: Int) // Menghapus data berdasarkan ID
}
