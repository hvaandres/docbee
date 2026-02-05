package us.docbee.docbeeapp.presentation.login.providers

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.koin.core.context.GlobalContext
import us.docbee.docbeeapp.domain.mappers.GoogleAuthErrorCodesMapper

class AndroidGoogleAuthProvider(
    private val context: Context
) : GoogleAuthProvider {
    override suspend fun getGoogleIdToken(): GoogleAuthResult {
        return try {
            val googleIdOption = GetSignInWithGoogleOption
                .Builder("add_server_client_id")
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = CredentialManager.create(context)
                .getCredential(request = request, context = context)

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                GoogleAuthResult.Success(GoogleIdTokenCredential.createFrom(credential.data).idToken)
            } else {
                GoogleAuthResult.Error
            }
        } catch (ex: Exception) {
            GoogleAuthErrorCodesMapper.eval(ex.localizedMessage)
        }
    }
}

actual fun getGoogleAuthProvider(): GoogleAuthProvider = AndroidGoogleAuthProvider(GlobalContext.get().get())
