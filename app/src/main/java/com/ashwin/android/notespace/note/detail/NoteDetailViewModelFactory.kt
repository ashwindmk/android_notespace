package com.ashwin.android.notespace.note.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashwin.android.notespace.model.repository.INoteRepository
import kotlinx.coroutines.Dispatchers

class NoteViewModelFactory(private val noteRepo: INoteRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteDetailViewModel(
            noteRepo,
            Dispatchers.Main
        ) as T
    }
}
