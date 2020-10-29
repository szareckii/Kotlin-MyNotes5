package com.szareckii.mynotes.ui.splash

import com.szareckii.mynotes.data.NotesRepository
import com.szareckii.mynotes.data.errors.NoAuthException
import com.szareckii.mynotes.ui.base.BaseViewModel

class SplashViewModel(val notesRepository: NotesRepository): BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser(){
        notesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let { SplashViewState(true) } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }

}