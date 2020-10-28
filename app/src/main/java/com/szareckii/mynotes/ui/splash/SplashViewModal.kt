package com.szareckii.mynotes.ui.splash

import com.szareckii.mynotes.data.NotesRepository
import com.szareckii.mynotes.data.errors.NoAuthException
import com.szareckii.mynotes.ui.base.BaseViewModel

class SplashViewModal: BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = it?.let { SplashViewState(true) } ?: let {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}