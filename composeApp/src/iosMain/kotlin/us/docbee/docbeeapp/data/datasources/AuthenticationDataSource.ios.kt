package us.docbee.docbeeapp.data.datasources

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.data.entities.LogoutResponse
import us.docbee.docbeeapp.data.entities.UserAuthResponse
import us.docbee.docbeeapp.data.entities.UserCreateResponse
import us.docbee.docbeeapp.wrappers.UserRemoteAuthentication

@OptIn(ExperimentalForeignApi::class, ExperimentalCoroutinesApi::class)
class IosAuthenticationDataSource() : AuthenticationDataSource {

    private val userAuthentication = UserRemoteAuthentication()

    override suspend fun authenticate(email: String, password: String): UserAuthResponse {
        return suspendCancellableCoroutine<UserAuthResponse> { thread ->
            userAuthentication.signInWithEmail(email = email, password = password) { result, error ->
                val authResponse = if (result != null) {
                    UserAuthResponse(uid = result["uid"].toString())
                } else {
                    val errorCode = error?.userInfo?.get("FIRAuthErrorUserInfoNameKey") as? String
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    UserAuthResponse(errorCode = errorCode, errorMessage = errorMessage)
                }
                thread.resume(authResponse, null)
            }
        }
    }

    override suspend fun authenticate(idToken: String): UserAuthResponse {
        return suspendCancellableCoroutine<UserAuthResponse> { thread ->
            userAuthentication.signInWithIdToken(idToken = idToken) { result, error ->
                val authResponse = if (result != null) {
                    UserAuthResponse(
                        uid = result["uid"].toString(),
                        isNewUser = result["isNewUser"].toString().toBoolean()
                    )
                } else {
                    val errorCode = error?.userInfo?.get("FIRAuthErrorUserInfoNameKey") as? String
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    UserAuthResponse(errorCode = errorCode, errorMessage = errorMessage)
                }
                thread.resume(authResponse, null)
            }
        }
    }

    override suspend fun signup(
        email: String,
        password: String
    ): UserCreateResponse {
        return suspendCancellableCoroutine<UserCreateResponse> { thread ->
            userAuthentication.signupWithEmail(email = email, password = password) { result, error ->
                val authResponse = if (result != null) {
                    UserCreateResponse(uid = result["uid"].toString())
                } else {
                    println("SIGNUP -> $error")
                    val errorCode = error?.userInfo?.get("FIRAuthErrorUserInfoNameKey") as? String
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    UserCreateResponse(errorCode = errorCode, errorMessage = errorMessage)
                }
                thread.resume(authResponse, null)
            }
        }
    }

    override suspend fun logout(): LogoutResponse {
        return suspendCancellableCoroutine<LogoutResponse> { thread ->
            userAuthentication.logoutWithCompletion { error ->
                val response = if (error == null) {
                    LogoutResponse.Success
                } else {
                    LogoutResponse.Error
                }
                thread.resume(response, null)
            }
        }
    }
}


actual fun getEmailAuth(): AuthenticationDataSource = IosAuthenticationDataSource()