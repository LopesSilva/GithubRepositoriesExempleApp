package com.example.githubrepositoriesapp.ui.main

import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.githubrepositoriesapp.R
import com.example.githubrepositoriesapp.adapters.RepoAdapter
import com.example.githubrepositoriesapp.adapters.RepoBoundaryCallback
import com.example.githubrepositoriesapp.data.PreferencesManager
import com.example.githubrepositoriesapp.data.repository.Repository
import com.example.githubrepositoriesapp.data.model.PullRequest
import com.example.githubrepositoriesapp.data.model.Repo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import java.lang.IllegalArgumentException
import kotlin.Exception
import kotlin.coroutines.coroutineContext

class SharedViewModel(private val repository: Repository, private val navController: NavController, private val preferenceManager:PreferencesManager): BaseViewModel() {

    val repos = MutableLiveData<State<Repo>>()
    val selectedRepo = MutableLiveData<Repo>()
    val prs = MutableLiveData<State<PullRequest>>()
    val refreshStatus = MutableLiveData<State<String>>()
    val appBarTitle = MutableLiveData<String>()

    //paging
    //val reposList: LiveData<PagedList<Repo>> = repository.loadAllReposForPaging().toLiveData(pageSize=10)

    private val pagedListConfig = PagedList.Config.Builder()
        .setPageSize(15)
        .setInitialLoadSizeHint(15)
        .setPrefetchDistance(10)
        .build()

    val reposList = LivePagedListBuilder(
        repository.loadAllReposForPaging(),
    pagedListConfig
    ).setBoundaryCallback(RepoBoundaryCallback(repository, viewModelScope,preferenceManager)).build()

    lateinit var prsList: LiveData<PagedList<PullRequest>>

    fun getRepos(){
        viewModelScope.launch {
            refreshRepos()
            repository.getRepos()
                .onStart {
                    repos.postValue(State<Repo>(State.STATES.Loading))
                }
                .catch {
                    repos.postValue(State<Repo>(State.STATES.Error,it.message))
                }
                .collect {
                    repos.postValue(State<Repo>(State.STATES.Success,"",it))
                }
        }
    }

    fun getPullRequests(login:String, repository:String){
        viewModelScope.launch {
            this@SharedViewModel.refreshPullRequests(login,repository)

            this@SharedViewModel.repository.getPullRequests(login,repository)
                .onStart {
                    prs.postValue(State<PullRequest>(State.STATES.Loading))
                }
                .catch {
                    prs.postValue(State<PullRequest>(State.STATES.Error,it.message))
                }
                .collect {
                    prs.postValue(State<PullRequest>(State.STATES.Success,"",it))
                }
        }
    }

    fun refreshRepos(){
        viewModelScope.launch{
            try {
                if(preferenceManager.giveUp){
                    //trava de segurança para não flodar rede
                    val exception = Exception("Bloqueio de Rede para evitar flood. Desinstale o app")
                    throw exception
                }
                repository.refreshAllRepos()
                refreshStatus.value= State(State.STATES.Success)
            }
            catch (e:Exception){
                refreshStatus.value= State(State.STATES.Error,e.message)
                preferenceManager.giveUp = true
            }
        }
    }

    fun refreshPullRequests(login: String, repository: String){
        viewModelScope.launch {
            try {
                this@SharedViewModel.repository.refreshPullRequest(login,repository)
                refreshStatus.value= State(State.STATES.Success)
            }
            catch (e:Exception){
                refreshStatus.value= State(State.STATES.Error,e.message)
            }
        }
    }

    fun setSelectedRepo(repo:Repo){
        if(selectedRepo.value?.id!=repo.id){
            selectedRepo.value = repo
            prs.postValue(State(State.STATES.Initiation))
        }
        prsList = repository.loadAllPullRequestsForPaging(selectedRepo.value!!.name,selectedRepo.value!!.owner.login).toLiveData(pageSize=10)
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    fun changeActionBarTitle(title: String) = appBarTitle.postValue(title)


}