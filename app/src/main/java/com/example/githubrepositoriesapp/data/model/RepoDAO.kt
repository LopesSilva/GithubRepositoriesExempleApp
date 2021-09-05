package com.example.githubrepositoriesapp.data.model

import androidx.paging.DataSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepo(repos: List<Repo>)

    @Delete
    fun deleteRepos(vararg repo: Repo): Int

    @Query("SELECT * FROM repos")
    fun loadAllRepos(): Flow<List<Repo>>


    @Query("SELECT * FROM repos")
    fun loadAllReposForPaging(): DataSource.Factory<Int, Repo>


}