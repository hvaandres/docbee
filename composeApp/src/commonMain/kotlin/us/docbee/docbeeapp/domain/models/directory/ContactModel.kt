package us.docbee.docbeeapp.domain.models.directory

data class ContactModel(
    var uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val address: String = "",
    val gender: String = ""
)