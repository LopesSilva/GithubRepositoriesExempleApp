package com.example.githubrepositoriesapp.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("stargazers_count")
    val stargazersCount: Long,
    @SerializedName("forks")
    val forks: Long,
    @SerializedName("language")
    val language: String,
    @SerializedName("html_url")
    val htmlURL: String,
    @SerializedName("description")
    val description: String?="",
    @SerializedName("owner")
    @Embedded
    val owner: Owner
)
