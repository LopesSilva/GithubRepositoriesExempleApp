package com.example.githubrepositoriesapp.data.model

import com.google.gson.annotations.SerializedName

data class RepoSearchResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("incomplete_results")
    val incompleteResults: String,
    @SerializedName("items")
    val items: List<Repo>
)
