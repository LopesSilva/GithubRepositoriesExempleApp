package com.example.githubrepositoriesapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubrepositoriesapp.databinding.TextRowItemBinding
import com.example.githubrepositoriesapp.data.model.PullRequest


class PullRequestAdapter(private val onClick: (PullRequest) -> Unit):
    ListAdapter<PullRequest, PullRequestAdapter.ViewHolder>(AsyncDifferConfig.Builder <PullRequest> (PullRequestDiffCallback()).build()) {

    class ViewHolder(var binding: TextRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(pullRequest: PullRequest, onClick: (PullRequest) -> Unit) {
            binding.repositoryNameListItemText.text = pullRequest.title
            binding.repositoryOwnerNameListItemText.text = pullRequest.login
            binding.repositoryDescriptionListItemText.text = pullRequest.body
            binding.repositoryDataItemText.text = pullRequest.data
            binding.repositoryForksListItemText.visibility = View.GONE
            binding.repositoryStarsListItemText.visibility = View.GONE

            Glide.with(binding.root.context)
                .load(pullRequest.avatarURLUser)
                .into(binding.profileListItemImageView)

            binding.card.setOnClickListener {
                onClick(pullRequest)
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = TextRowItemBinding.inflate(LayoutInflater.from(viewGroup.context))

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


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

         viewHolder.bindView(currentList[position], onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            for(payload in payloads){
                when(payload) {
                    "body" -> {
                        holder.binding.repositoryDescriptionListItemText.text = getItem(position).body
                    }
                    "data"-> {
                        holder.binding.repositoryDataItemText.text = getItem(position).data
                    }
                    "login"-> {
                        holder.binding.repositoryNameListItemText.text = getItem(position).login
                    }
                    "avatarURL"-> {
                        Glide.with(holder.binding.root.context)
                            .load(getItem(position).avatarURLUser)
                            .into(holder.binding.profileListItemImageView)
                    }
                    "title"-> {
                        holder.binding.repositoryNameListItemText.text = getItem(position).title
                    }
                    else -> super.onBindViewHolder(holder, position, payloads)
                }

            }
        }
    }

    override fun getItemCount() = currentList.size

}

private class PullRequestDiffCallback : DiffUtil.ItemCallback<PullRequest>() {

    override fun areItemsTheSame(oldItem: PullRequest, newItem: PullRequest) =
        oldItem.pullRequestId == newItem.pullRequestId


    override fun areContentsTheSame(oldItem: PullRequest, newItem: PullRequest) =
        oldItem.toString() == newItem.toString()

    override fun getChangePayload(oldItem: PullRequest, newItem: PullRequest): Any? {
        val diff = Bundle()

        if(oldItem.body!=newItem.body){
            diff.putString("body",newItem.body)
        }
        if(oldItem.data!=newItem.data){
            diff.putString("data",newItem.data)
        }
        if(oldItem.login!=newItem.login){
            diff.putString("login",newItem.login)

        }
        if(oldItem.avatarURLUser!=newItem.avatarURLUser){
            diff.putString("avatarURL",newItem.avatarURLUser)
        }

        if(oldItem.url!=newItem.url){
            diff.putString("url",newItem.url)

        }
        if(oldItem.title!=newItem.title){
            diff.putString("title",newItem.title)

        }
        if(diff.isEmpty){
            return null
        }
        return diff
    }
}