package com.ashwin.android.notespace.note.list

import androidx.recyclerview.widget.DiffUtil
import com.ashwin.android.notespace.model.Note

class NoteDiffUtilCallback : DiffUtil.ItemCallback<Note>(){
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.contents == newItem.contents
    }
}
