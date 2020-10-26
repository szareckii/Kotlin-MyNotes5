package com.szareckii.mynotes.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.szareckii.mynotes.data.NotesRepository
import com.szareckii.mynotes.data.entity.Note
import com.szareckii.mynotes.data.model.NoteResult
import com.szareckii.mynotes.ui.base.BaseViewModel

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer {result: NoteResult? ->
        result ?: return@Observer
        when(result) {
            is NoteResult.Success<*> -> viewStateLiveData.value = MainViewState(result.data as? List<Note>)
            is NoteResult.Error -> viewStateLiveData.value = MainViewState(error = result.error)
        }
    }

    private val repositoryNotes = NotesRepository.getNotes()

    init {
        repositoryNotes.observeForever(notesObserver)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData

    override fun onCleared() {
        super.onCleared()
        repositoryNotes.removeObserver(notesObserver)
    }
}