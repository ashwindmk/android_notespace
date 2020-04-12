package com.ashwin.android.notespace.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<T>(override val coroutineContext: CoroutineContext) : ViewModel(), CoroutineScope {
    abstract fun handleEvent(event: T)

    protected val errorState = MutableLiveData<Event<String>>()
    val error: LiveData<Event<String>> get() = errorState
}
