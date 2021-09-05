package com.example.githubrepositoriesapp.data.db

import androidx.room.RoomDatabase
import androidx.room.Database
import android.content.Context
import androidx.room.Room
import com.example.githubrepositoriesapp.data.model.*

@Database(entities = arrayOf(Repo::class, Owner::class, PullRequest::class, User::class),version = 1)
abstract class DatabaseHelper:RoomDatabase() {

    abstract fun ownerDao(): OwnerDAO
    abstract fun repoDao(): RepoDAO
    abstract fun userDao(): UserDAO
    abstract fun pullDao(): PullRequestDAO

    companion object {
        private var INSTANCE: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper? {
            if (INSTANCE == null) {
                synchronized(DatabaseHelper::class) {
                    INSTANCE = Room.databaseBuilder(context, DatabaseHelper::class.java, "routerdb")
                        //.allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}