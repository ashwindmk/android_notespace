package com.ashwin.android.notespace.model.repository

import com.ashwin.android.notespace.model.Note
import com.ashwin.android.notespace.common.Result

interface INoteRepository {
    suspend fun getNoteById(noteId: String): Result<Exception, Note>
    suspend fun getNotes(): Result<Exception, List<Note>>
    suspend fun deleteNote(note: Note): Result<Exception, Unit>
    suspend fun updateNote(note: Note): Result<Exception, Unit>
}
