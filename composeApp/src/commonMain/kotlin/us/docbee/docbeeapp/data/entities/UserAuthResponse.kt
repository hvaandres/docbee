package us.docbee.docbeeapp.data.entities

data class UserAuthResponse(
    val uid: String? = null,
    val isNewUser: Boolean = false,
    val errorCode: String? = null,
    val errorMessage: String? = null
)