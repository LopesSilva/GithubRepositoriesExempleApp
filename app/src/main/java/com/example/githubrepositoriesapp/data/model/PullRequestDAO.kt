package com.example.githubrepositoriesapp.data.model

import androidx.paging.DataSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PullRequestDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPullRequest(pullRequests: List<PullRequest>)

    @Delete
    fun deleteRepos(vararg pullRequests: PullRequest): Int

    @Query("SELECT * FROM prs WHERE name = :repository")
    fun loadAllPullRequest(repository: String): Flow<List<PullRequest>>

    @Query("SELECT * FROM prs WHERE name = :repository")
    fun loadAllPullRequestforPaging(repository: String): DataSource.Factory<Int, PullRequest>
}