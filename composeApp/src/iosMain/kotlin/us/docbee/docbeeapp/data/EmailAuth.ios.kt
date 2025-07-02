package us.docbee.docbeeapp.data

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIREmailAuthProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.data.entities.UserAuthResponse
import us.docbee.docbeeapp.data.entities.UserCreateResponse

@OptIn(ExperimentalForeignApi::class, ExperimentalCoroutinesApi::class)
class IosEmailAuth() : EmailAuth {

    override suspend fun authenticate(email: String, password: String): UserAuthResponse {
        return suspendCancellableCoroutine<UserAuthResponse> { thread ->
            FIRAuth.auth().signInWithCredential(
                credential = FIREmailAuthProvider.credentialWithEmail(
                    email = email,
                    password = password
                )
            ) { result, error ->
                val authResponse = if (result != null) {
                    UserAuthResponse.Success(result.user().uid())
                } else {
                    println(error)
                    val errorCode = error?.userInfo?.get("FIRAuthErrorUserInfoNameKey") as? String
                    AuthErrorCodesMapper.eval(errorCode)
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
            FIRAuth.auth().createUserWithEmail(email, password) { result, error ->
                val authResponse = if (result != null) {
                    UserCreateResponse.Success(result.user().uid())
                } else {
                    val errorCode = error?.userInfo?.get("FIRAuthErrorUserInfoNameKey") as? String
                    CreateErrorCodesMapper.eval(errorCode)
                }
                thread.resume(authResponse, null)
            }
        }
    }
}


actual fun getEmailAuth(): EmailAuth = IosEmailAuth()