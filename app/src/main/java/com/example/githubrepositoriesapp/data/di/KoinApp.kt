package com.example.githubrepositoriesapp.data.di

import android.app.Application
import com.example.githubrepositoriesapp.data.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class KoinApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(this@KoinApp)
            // modules
            modules(listOf(viewModel1Module,reposRepositoryModule,gitHubAPIModule,networkModule,daoRepoModule,daoPullModule,sharedPreferenceModule))
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}