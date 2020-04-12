package com.ashwin.android.notespace.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashwin.android.notespace.model.repository.IUserRepository
import kotlinx.coroutines.Dispatchers

class UserViewModelFactory(private val userRepo: IUserRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userRepo, Dispatchers.Main) as T
    }
}
