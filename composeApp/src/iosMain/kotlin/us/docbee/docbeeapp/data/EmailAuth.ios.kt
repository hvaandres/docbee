package us.docbee.docbeeapp.data

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIREmailAuthProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class IosEmailAuth() : EmailAuth {

    @OptIn(ExperimentalForeignApi::class, ExperimentalCoroutinesApi::class)
    override suspend fun authenticate(email: String, password: String): Boolean {
        return suspendCancellableCoroutine<Boolean> { thread ->
            FIRAuth.auth().signInWithCredential(
                credential = FIREmailAuthProvider.credentialWithEmail(
                    email = email,
                    password = password
                )
            ) { result, error ->
                if (result != null) {
                    thread.resume(true, null)
                } else {
                    thread.resume(false, null)
                }
            }
        }
    }
}

actual fun getEmailAuth(): EmailAuth = IosEmailAuth()