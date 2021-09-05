package com.example.githubrepositoriesapp.data

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {

    private const val NAME = "GitHubRepositoriesApp"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    private val PAGE = "page"
    private val PER_PAGE = "perPage"
    private val GIVE_UP = "giveUp"


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

     var page: Int
        get() = preferences.getInt(PAGE, 1)
        set(value) {
            preferences.edit().putInt(PAGE, value).apply();
        }

    var perPage: Int
        get() = preferences.getInt(PER_PAGE, 50)
        set(value) {
            preferences.edit().putInt(PER_PAGE, value).apply();
        }

    var giveUp: Boolean
        get() = preferences.getBoolean(GIVE_UP, false)
        set(value) {
            preferences.edit().putBoolean(GIVE_UP, value).apply();
        }
}