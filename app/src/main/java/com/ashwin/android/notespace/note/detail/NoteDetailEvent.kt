package com.ashwin.android.notespace.note.detail

sealed class NoteDetailEvent {
    data class OnDoneClick(val contents: String) : NoteDetailEvent()
    object OnDeleteClick : NoteDetailEvent()
    object OnDeleteConfirmed : NoteDetailEvent()
    data class OnStart(val noteId: String) : NoteDetailEvent()
}
