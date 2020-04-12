package com.ashwin.android.notespace.note.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashwin.android.notespace.model.repository.INoteRepository
import kotlinx.coroutines.Dispatchers

class NoteListViewModelFactory(private val noteRepo: INoteRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NoteListViewModel(noteRepo, Dispatchers.Main) as T
    }
}
