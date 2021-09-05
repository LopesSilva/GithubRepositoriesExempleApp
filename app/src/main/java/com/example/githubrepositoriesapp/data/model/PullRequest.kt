package com.example.githubrepositoriesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "prs")
data class PullRequest(
    val url:String,
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val pullRequestId:Long,
    val state:String,
    val title:String,
    @SerializedName("created_at")
    val data:String,
    val body:String?="",
    val login:String,
    val name: String,
    val avatarURLUser: String
)

