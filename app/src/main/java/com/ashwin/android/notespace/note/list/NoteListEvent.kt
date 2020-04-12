package com.ashwin.android.notespace.note.list

sealed class NoteListEvent {
    data class OnNoteItemClick(val position: Int) : NoteListEvent()
    object OnNewNoteClick : NoteListEvent()
    object OnStart : NoteListEvent()
}
