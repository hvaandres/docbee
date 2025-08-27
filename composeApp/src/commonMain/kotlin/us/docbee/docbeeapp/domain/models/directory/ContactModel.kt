package us.docbee.docbeeapp.domain.models.directory

data class ContactModel(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val gender: String = ""
)