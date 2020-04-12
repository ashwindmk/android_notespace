package com.ashwin.android.notespace.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ashwin.android.notespace.model.repositoryimpl.FirebaseUserRepositoryImpl
import com.ashwin.android.notespace.model.repository.IUserRepository
import com.google.firebase.FirebaseApp

class LoginInjector(application: Application): AndroidViewModel(application) {
    init {
        FirebaseApp.initializeApp(application)
    }

    private fun getUserRepository(): IUserRepository {
        return FirebaseUserRepositoryImpl()
    }

    fun provideUserViewModelFactory(): UserViewModelFactory {
        return UserViewModelFactory(getUserRepository())
    }
}
