package com.example.githubrepositoriesapp.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg owner: Owner)

    @Update
    fun updateUser(owner: Owner): Int

    @Delete
    fun deleteUsers(vararg owner: Owner): Int

    @Query("SELECT * FROM owners")
    fun loadAllUsers(): Flow<List<Owner>>
}