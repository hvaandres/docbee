package us.docbee.docbeeapp.presentation.login.providers

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.domain.mappers.GoogleAuthErrorCodesMapper
import us.docbee.docbeeapp.wrappers.GoogleAuthRemoteProvider

@OptIn(ExperimentalForeignApi::class)
class IosGoogleAuthProvider: GoogleAuthProvider {

    private val googleAuthProvider = GoogleAuthRemoteProvider()

    override suspend fun getGoogleIdToken(): GoogleAuthResult {
        return suspendCancellableCoroutine<GoogleAuthResult> { thread ->
            googleAuthProvider.getGoogleIdTokenWithCompletion { result, error ->
                val response = if (!result.isNullOrEmpty()) {
                    GoogleAuthResult.Success(result)
                } else {
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    GoogleAuthErrorCodesMapper.eval(errorMessage)
                }
                thread.resume(response, null)
            }
        }
    }
}

actual fun getGoogleAuthProvider(): GoogleAuthProvider = IosGoogleAuthProvider()