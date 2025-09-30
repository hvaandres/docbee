package us.docbee.docbeeapp.data

import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import us.docbee.docbeeapp.data.entities.LogoutResponse
import us.docbee.docbeeapp.data.entities.UserAuthResponse
import us.docbee.docbeeapp.data.entities.UserCreateResponse

@OptIn(ExperimentalCoroutinesApi::class)
class AndroidEmailAuth : EmailAuth {

    override suspend fun authenticate(email: String, password: String): UserAuthResponse {
        return try {
            val authResult = Firebase.auth
                .signInWithCredential(EmailAuthProvider.getCredential(email, password))
                .await()

            authResult.user?.let { user ->
                UserAuthResponse(uid = user.uid)
            } ?: UserAuthResponse(errorCode = "UNKNOWN_ERROR", errorMessage = "User is null.")
        } catch (ex: FirebaseAuthInvalidCredentialsException) {
            UserAuthResponse(errorCode = "ERROR_INVALID_CREDENTIAL", errorMessage = ex.localizedMessage)
        } catch (ex: Exception) {
            UserAuthResponse(errorCode = "UNKNOWN_ERROR", errorMessage = ex.localizedMessage)
        }
    }

    override suspend fun signup(email: String, password: String): UserCreateResponse {
        return try {
            val creationResult = Firebase.auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            creationResult.user?.let { user ->
                UserCreateResponse(uid = user.uid)
            } ?: UserCreateResponse(errorCode = "UNKNOWN_ERROR", errorMessage = "User is null.")
        } catch (ex: FirebaseAuthUserCollisionException) {
            UserCreateResponse(errorCode = "ERROR_EMAIL_ALREADY_IN_USE", errorMessage = ex.localizedMessage)
        } catch (ex: FirebaseAuthWeakPasswordException) {
            UserCreateResponse(errorCode = "ERROR_WEAK_PASSWORD", errorMessage = ex.localizedMessage)
        } catch (ex: FirebaseException) {
            UserCreateResponse(errorCode = "UNKNOWN_ERROR", errorMessage = ex.localizedMessage)
        } catch (ex: Exception) {
            UserCreateResponse(errorCode = "UNKNOWN_ERROR", errorMessage = ex.localizedMessage)
        }
    }

    override suspend fun logout(): LogoutResponse {
        Firebase.auth.signOut()
        return LogoutResponse.Success
    }
}

actual fun getEmailAuth(): EmailAuth = AndroidEmailAuth()