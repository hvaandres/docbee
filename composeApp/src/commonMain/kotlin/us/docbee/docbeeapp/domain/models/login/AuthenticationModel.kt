package us.docbee.docbeeapp.domain.models.login

data class AuthenticationModel(
    val idToken: String = "",
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)