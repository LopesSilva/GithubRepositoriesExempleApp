package com.example.githubrepositoriesapp.data.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(vararg user: User)

    @Update
    fun updateUser(user: User): Int

    @Delete
    fun deleteUsers(vararg user: User): Int

    @Query("SELECT * FROM users")
    fun loadAllUsers(): Flow<List<User>>
}