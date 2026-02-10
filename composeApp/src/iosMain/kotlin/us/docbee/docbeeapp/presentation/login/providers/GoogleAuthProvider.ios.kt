package us.docbee.docbeeapp.presentation.login.providers

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.domain.mappers.GoogleAuthErrorCodesMapper
import us.docbee.docbeeapp.domain.models.login.AuthenticationModel
import us.docbee.docbeeapp.wrappers.GoogleAuthRemoteProvider

@OptIn(ExperimentalForeignApi::class)
class IosGoogleAuthProvider: GoogleAuthProvider {

    private val googleAuthProvider = GoogleAuthRemoteProvider()

    @Suppress("CAST_NEVER_SUCCEEDS")
    override suspend fun getGoogleAuthData(): GoogleAuthResult {
        return suspendCancellableCoroutine { thread ->
            googleAuthProvider.getGoogleAuthDataWithCompletion { result, error ->
                val response = if (result != null) {
                    GoogleAuthResult.Success(result as AuthenticationModel )
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