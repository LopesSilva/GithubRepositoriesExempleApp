package com.example.githubrepositoriesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(@PrimaryKey(autoGenerate = false)
                @SerializedName("id")
                val idUser:Long,
                @SerializedName("login")
                val loginUser: String,
                @SerializedName("avatar_url")
                val avatarURLUser: String)


