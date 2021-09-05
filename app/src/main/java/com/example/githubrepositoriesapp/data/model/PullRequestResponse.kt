package com.example.githubrepositoriesapp.data.model

import com.google.gson.annotations.SerializedName
data class PullRequestResponse(
    @SerializedName("html_url")
    val url:String,
    @SerializedName("id")
    val pullRequestId:Long,
    val user:User,
    val state:String,
    val title:String,
    @SerializedName("created_at")
    val data:String,
    val body:String,
    val base:Base
){

}