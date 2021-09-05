package com.example.githubrepositoriesapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubrepositoriesapp.data.PreferencesManager
import com.example.githubrepositoriesapp.databinding.TextRowItemBinding
import com.example.githubrepositoriesapp.data.model.Repo
import com.example.githubrepositoriesapp.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class RepoAdapter(private val onClick: (Repo) -> Unit) :
    PagedListAdapter<Repo,RepoAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(var binding: TextRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(repo: Repo, onClick: (Repo) -> Unit) {
            binding.repositoryNameListItemText.text = repo.name
            binding.repositoryOwnerNameListItemText.text = repo.owner.login
            binding.repositoryDescriptionListItemText.text = repo.description
            binding.repositoryForksListItemText.text = repo.forks.toString()
            binding.repositoryStarsListItemText.text = repo.stargazersCount.toString()
            binding.repositoryDataItemText.visibility = View.GONE

            Glide.with(binding.root.context)
                .load(repo.owner.avatarURL)
                .into(binding.profileListItemImageView)

            binding.card.setOnClickListener {
                onClick(repo)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo: Repo? = getItem(position)
        if (repo != null) {
            holder.bindView(repo,onClick)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TextRowItemBinding.inflate(LayoutInflater.from(parent.context))

        binding.root.apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT)

            val param = layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,8,0,8)

            layoutParams = param
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for(payload in payloads){
                when(payload) {
                    "description" -> {
                        holder.binding.repositoryDescriptionListItemText.text = getItem(position)?.description
                    }
                    "forks"-> {
                        holder.binding.repositoryForksListItemText.text = getItem(position)?.forks.toString()
                    }
                    "name"-> {
                        holder.binding.repositoryNameListItemText.text = getItem(position)?.name
                    }
                    "avatarURL"-> {
                        Glide.with(holder.binding.root.context)
                            .load(getItem(position)?.owner?.avatarURL)
                            .into(holder.binding.profileListItemImageView)
                    }
                    "login"-> {
                        holder.binding.repositoryOwnerNameListItemText.text = getItem(position)?.owner?.login
                    }
                    "stargazersCount"-> {
                        holder.binding.repositoryStarsListItemText.text = getItem(position)?.stargazersCount.toString()
                    }
                    else -> super.onBindViewHolder(holder, position, payloads)
                }

            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo,
                                         newItem: Repo) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repo,
                                            newItem: Repo) = oldItem == newItem
        }
    }


}

class RepoBoundaryCallback(val repository: Repository, val coroutineScope: CoroutineScope, val preferencesManager: PreferencesManager): PagedList.BoundaryCallback<Repo>() {
    var isRequesting:Boolean = false

    override fun onZeroItemsLoaded() {
        coroutineScope.launch {
            requestMoreData()
        }
    }


    override fun onItemAtEndLoaded(itemAtEnd: Repo) {
        coroutineScope.launch {
            requestMoreData()
        }
    }

    @Synchronized
    private suspend fun requestMoreData() = withContext(Dispatchers.IO){
        if(isRequesting || preferencesManager.giveUp){
            return@withContext;
        }
        isRequesting = true

            try {
                repository.refreshRepos(preferencesManager.page, preferencesManager.perPage)
                preferencesManager.page++
                isRequesting = false
            }
            catch (e:Exception){
                Log.e("ERROR", e.printStackTrace().toString())
                isRequesting = false
                preferencesManager.giveUp = true
            }

    }

}

/*
class DiffCallback : DiffUtil.ItemCallback<Repo>() {

    override fun areItemsTheSame(oldItem: Repo, newItem: Repo) =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Repo, newItem: Repo) =
        oldItem.toString() == newItem.toString()

    override fun getChangePayload(oldItem: Repo, newItem: Repo): Any? {
        val diff = Bundle()

        if(oldItem.description!=newItem.description){
            diff.putString("description",newItem.description)
        }
        if(oldItem.forks!=newItem.forks){
            diff.putLong("forks",newItem.forks)
        }
        if(oldItem.name!=newItem.name){
            diff.putString("name",newItem.name)

        }
        if(oldItem.owner.avatarURL!=newItem.owner.avatarURL){
            diff.putString("avatarURL",newItem.owner.avatarURL)

        }
        if(oldItem.owner.login!=newItem.owner.login){
            diff.putString("login",newItem.owner.login)

        }
        if(oldItem.stargazersCount!=newItem.stargazersCount){
            diff.putLong("stargazersCount",newItem.stargazersCount)
        }
        if(diff.isEmpty){
            return null
        }
        return diff
    }
}*/
