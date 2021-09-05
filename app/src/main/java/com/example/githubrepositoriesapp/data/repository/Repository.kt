package com.example.githubrepositoriesapp.data.repository

import androidx.paging.DataSource
import com.example.githubrepositoriesapp.data.model.PullRequest
import com.example.githubrepositoriesapp.data.model.PullRequestDAO
import com.example.githubrepositoriesapp.data.model.Repo
import com.example.githubrepositoriesapp.data.model.RepoDAO
import com.example.githubrepositoriesapp.api.GitHubEndpointAPI
import com.example.githubrepositoriesapp.data.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class Repository (private val gitHubEndpointAPI: GitHubEndpointAPI, private val repoDAO: RepoDAO, private val pullRequestDAO: PullRequestDAO, private val preferenceManager: PreferencesManager) {

    fun getRepos(): Flow<List<Repo>> {
        return repoDAO.loadAllRepos()
    }

    fun loadAllReposForPaging(): DataSource.Factory<Int, Repo> {
        return repoDAO.loadAllReposForPaging()
    }

    fun loadAllPullRequestsForPaging(login:String, repository:String): DataSource.Factory<Int, PullRequest> {
        return pullRequestDAO.loadAllPullRequestforPaging(repository)
    }

    fun getPullRequests(login:String, repository:String): Flow<List<PullRequest>> {
        return pullRequestDAO.loadAllPullRequest(repository)
    }

    suspend fun refreshRepos(page:Int, perPage:Int) = withContext(Dispatchers.IO){
            val repoSearchResponse = gitHubEndpointAPI.getRepoByLanguage(page = page, perPage = perPage)
            repoDAO.insertRepo(repoSearchResponse.items)
    }

    suspend fun refreshAllRepos() = withContext(Dispatchers.IO){
        var total = preferenceManager.page * preferenceManager.perPage
        var perPage = if (total < 100) total else 100
        val repoSearchResponse = gitHubEndpointAPI.getRepoByLanguage(page = 1,perPage = perPage)
        repoDAO.insertRepo(repoSearchResponse.items)
    }

    suspend fun refreshPullRequest(login:String, repository:String) = withContext(Dispatchers.IO){
        val pullRequestResponseList = gitHubEndpointAPI.getPullRequests(login, repository)
        if (pullRequestResponseList.isNotEmpty()) {
            var pullRequestList = ArrayList<PullRequest>()
            pullRequestResponseList.forEach {
                pullRequestList.add(PullRequest(it.url,it.pullRequestId,it.state,it.title,it.data,it.body,it.user.loginUser,it.base.repo.name,it.user.avatarURLUser))
            }
            pullRequestDAO.insertPullRequest(pullRequestList)
        }
    }

}