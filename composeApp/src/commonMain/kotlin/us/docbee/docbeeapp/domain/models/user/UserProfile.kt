package us.docbee.docbeeapp.domain.models.user

data class UserProfile(
    val uid: String,
    val name: String,
    val lastname: String,
    val dateOfBirth: String,
    val phoneNumber: String,
    val email: String,
    val createdAt: Long
)