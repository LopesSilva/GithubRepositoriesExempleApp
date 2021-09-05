package com.example.githubrepositoriesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "owners")
data class Owner(@PrimaryKey(autoGenerate = false)
                @SerializedName("id")
                val idOwner:Long,
                 @SerializedName("login")
                val login: String,
                 @SerializedName("avatar_url")
                val avatarURL: String)
