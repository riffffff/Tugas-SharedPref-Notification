package com.example.mytugas_api.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DogDao {
    @Query("SELECT * FROM favorite_dogs")
    fun getAll(): List<DogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dog: DogEntity)

    @Query("DELETE FROM favorite_dogs WHERE id = :id")
    fun deleteById(id: Int)
}
