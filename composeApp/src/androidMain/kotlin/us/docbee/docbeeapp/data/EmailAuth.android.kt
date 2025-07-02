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
                UserAuthResponse.Success(uid = user.uid)
            } ?: UserAuthResponse.Error
        } catch (ex: FirebaseAuthInvalidCredentialsException) {
            UserAuthResponse.InvalidCredentials
        } catch (ex: Exception) {
            UserAuthResponse.Error
        }
    }

    override suspend fun signup(email: String, password: String): UserCreateResponse {
        return try {
            val creationResult = Firebase.auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            creationResult.user?.let { user ->
                UserCreateResponse.Success(user.uid)
            } ?: UserCreateResponse.Error
        } catch (ex: FirebaseAuthUserCollisionException) {
            UserCreateResponse.AlreadyUsed
        } catch (ex: FirebaseAuthWeakPasswordException) {
            UserCreateResponse.WeakPassword
        } catch (ex: FirebaseException) {
            UserCreateResponse.WeakPassword
        } catch (ex: Exception) {
            UserCreateResponse.Error
        }
    }
}

actual fun getEmailAuth(): EmailAuth = AndroidEmailAuth()