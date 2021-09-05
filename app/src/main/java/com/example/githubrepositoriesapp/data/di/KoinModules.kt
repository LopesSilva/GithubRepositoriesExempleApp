package com.example.githubrepositoriesapp.data.di


import android.content.Context
import androidx.navigation.NavController
import com.example.githubrepositoriesapp.ui.main.SharedViewModel
import com.example.githubrepositoriesapp.data.repository.Repository
import com.example.githubrepositoriesapp.data.db.DatabaseHelper
import com.example.githubrepositoriesapp.data.model.PullRequestDAO

import com.example.githubrepositoriesapp.data.model.RepoDAO
import com.example.githubrepositoriesapp.api.GitHubEndpointAPI
import com.example.githubrepositoriesapp.data.PreferencesManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.androidx.viewmodel.dsl.viewModel


val viewModel1Module = module {
    viewModel { (navController:NavController) ->
        SharedViewModel(get(), navController = navController, get()) }
}

val reposRepositoryModule = module {
    fun provideUserRepository(api: GitHubEndpointAPI, repoDAO: RepoDAO, pullRequestDAO: PullRequestDAO, preferencesManager:PreferencesManager): Repository {
        return Repository(api, repoDAO, pullRequestDAO,preferencesManager)
    }

    single { Repository(get(), get(), get(),get()) }
}

//Modulo da API
val gitHubAPIModule = module {
    fun provideUserApi(retrofit: Retrofit): GitHubEndpointAPI {
        return retrofit.create(GitHubEndpointAPI::class.java)
    }
    single { provideUserApi(get()) }
}

val networkModule = module {

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()


        return okHttpClientBuilder.build()
    }

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }


    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single { provideGson() }
    single { provideRetrofit(get(), get()) }

}

val daoRepoModule = module {
    single { DatabaseHelper.getInstance(androidContext())?.repoDao() }
}

val daoPullModule = module {
    single { DatabaseHelper.getInstance(androidContext())?.pullDao() }
}

val sharedPreferenceModule = module {

    fun provideSharedPreference(context: Context): PreferencesManager {
        PreferencesManager.init(context)
        return PreferencesManager
    }

    single { provideSharedPreference(androidContext()) }

}





