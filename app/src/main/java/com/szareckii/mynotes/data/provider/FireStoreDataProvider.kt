package com.szareckii.mynotes.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.szareckii.mynotes.data.entity.Note
import com.szareckii.mynotes.data.entity.User
import com.szareckii.mynotes.data.errors.NoAuthException
import com.szareckii.mynotes.data.model.NoteResult

class FireStoreDataProvider: DataProvider {
    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USERS_COLLECTION = "users"
    }

    private val store by lazy { FirebaseFirestore.getInstance() }
    private val notesReference
        get() = currentUser?.let { store.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION) } ?: throw NoAuthException()

    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    override fun getCurrentUser() = MutableLiveData<User?>().apply {
        value = currentUser?.let { User(it.displayName ?: "", it.email ?: "") }
    }

    override fun getNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener { snapshot, error ->
            error?.let {
                result.value = NoteResult.Error(it)
                return@addSnapshotListener
            }
            snapshot?.let {
                val notes = it.documents.map { it.toObject(Note::class.java) as Note}
                result.value = NoteResult.Success(notes)
            }
        }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id).set(note)
                .addOnSuccessListener {
                    result.value = NoteResult.Success(note)
                }.addOnFailureListener{
                    result.value = NoteResult.Error(it)
                }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
                .addOnSuccessListener {snapshot ->
                    val note = snapshot.toObject(Note::class.java) as Note
                    result.value = NoteResult.Success(note)
                }.addOnFailureListener{
                    result.value = NoteResult.Error(it)
                }
        return result

    }
}