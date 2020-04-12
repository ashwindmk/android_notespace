package com.ashwin.android.notespace.note.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ashwin.android.notespace.model.RoomNoteDatabase
import com.ashwin.android.notespace.model.repositoryimpl.NoteRepoImpl
import com.ashwin.android.notespace.model.repository.INoteRepository
import com.google.firebase.FirebaseApp

class NoteDetailInjector(application: Application): AndroidViewModel(application) {
    private fun getNoteRepository(): INoteRepository {
        FirebaseApp.initializeApp(getApplication())
        return NoteRepoImpl(local = RoomNoteDatabase.getInstance(getApplication()).roomNoteDao())
    }

    fun provideNoteViewModelFactory(): NoteViewModelFactory = NoteViewModelFactory(getNoteRepository())
}
