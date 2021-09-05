package com.example.githubrepositoriesapp.ui.main

import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {


     class State<T>(var state: STATES? = STATES.Initiation, var message: String? = "", var data:List<T> = emptyList<T>()){
        enum class STATES {
            Initiation, Loading, Error, Success
        }
    }
}