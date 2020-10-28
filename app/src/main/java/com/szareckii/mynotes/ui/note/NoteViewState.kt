package com.szareckii.mynotes.ui.note

import com.szareckii.mynotes.data.entity.Note
import com.szareckii.mynotes.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error) {
}