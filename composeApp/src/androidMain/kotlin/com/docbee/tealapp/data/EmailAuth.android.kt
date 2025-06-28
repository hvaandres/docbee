package com.docbee.tealapp.data

import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class AndroidEmailAuth : EmailAuth {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun authenticate(email: String, password: String): Boolean {
        return suspendCancellableCoroutine<Boolean> { thread ->
            Firebase.auth.signInWithCredential(EmailAuthProvider.getCredential(email, password))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        thread.resume(true, null)
                    } else {
                        thread.resume(false, null)
                    }
                }
        }
    }
}

actual fun getEmailAuth(): EmailAuth = AndroidEmailAuth()