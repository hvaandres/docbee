package us.docbee.docbeeapp.presentation.login.providers

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.wrappers.GoogleAuthRemoteProvider

@OptIn(ExperimentalForeignApi::class)
class IosGoogleAuthProvider: GoogleAuthProvider {

    private val googleAuthProvider = GoogleAuthRemoteProvider()

    override suspend fun getGoogleIdToken(): String {
        return suspendCancellableCoroutine<String> { thread ->
            googleAuthProvider.getGoogleIdTokenWithCompletion { result, _ ->
                val authResponse = result ?: ""
                thread.resume(authResponse, null)
            }
        }
    }
}

actual fun getGoogleAuthProvider(): GoogleAuthProvider = IosGoogleAuthProvider()