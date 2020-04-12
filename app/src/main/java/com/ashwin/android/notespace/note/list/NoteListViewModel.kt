package com.ashwin.android.notespace.note.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ashwin.android.notespace.common.*
import com.ashwin.android.notespace.common.GET_NOTES_ERROR
import com.ashwin.android.notespace.model.Note
import com.ashwin.android.notespace.model.repository.INoteRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NoteListViewModel(val noteRepo: INoteRepository, uiContext: CoroutineContext) : BaseViewModel<NoteListEvent>(uiContext) {
    private val noteListState = MutableLiveData<List<Note>>()
    val noteList: MutableLiveData<List<Note>> get() = noteListState

    private val editNoteState = MutableLiveData<Event<String>>()
    val editNote: LiveData<Event<String>> get() = editNoteState

    override fun handleEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnStart -> getNotes()
            is NoteListEvent.OnNoteItemClick -> {
                editNote(event.position)
            }
        }
    }

    private fun editNote(position: Int) {
        editNoteState.value = Event<String>(noteList.value!![position].creationDate)
    }

    private fun getNotes() = launch {
        val notesResult = noteRepo.getNotes()
        when (notesResult) {
            is Result.Value -> noteListState.value = notesResult.value
            is Result.Error -> errorState.value = Event<String>(GET_NOTES_ERROR)
        }
    }
}
