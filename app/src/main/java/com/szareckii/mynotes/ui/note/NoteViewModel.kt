package com.szareckii.mynotes.ui.note

import androidx.lifecycle.LiveData
import com.szareckii.mynotes.data.NotesRepository
import com.szareckii.mynotes.data.entity.Note
import com.szareckii.mynotes.data.model.NoteResult
import com.szareckii.mynotes.ui.base.BaseViewModel
import androidx.lifecycle.Observer

class NoteViewModel : BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    private var pendingNote: Note? = null
    private var noteLiveData: LiveData<NoteResult>? = null

    private val noteObserver = object : Observer<NoteResult> {
        override fun onChanged(result: NoteResult?) {
            when (result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(result.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
            noteLiveData?.removeObserver(this)
        }
    }

    fun save(note : Note) {
        pendingNote = note
    }

    override fun onCleared() {
        pendingNote?.let {
            NotesRepository.saveNote(it)
        }
        noteLiveData?.removeObserver(noteObserver)
    }

    fun loadNote(id: String) {
        noteLiveData = NotesRepository.getNoteById(id)
        noteLiveData?.observeForever(noteObserver)
    }
}