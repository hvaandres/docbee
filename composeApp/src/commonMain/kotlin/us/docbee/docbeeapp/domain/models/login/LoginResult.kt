package us.docbee.docbeeapp.domain.models.login

sealed class LoginResult {
    data class Success(val uid: String): LoginResult()
    data object InvalidCredentials: LoginResult()
    data object Error: LoginResult()
    data object InvalidEmail: LoginResult()
    data object InvalidPassword: LoginResult()
    data object InvalidEmailAndPassword: LoginResult()
}