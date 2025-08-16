package us.docbee.docbeeapp.domain.mappers

import us.docbee.docbeeapp.domain.models.signup.SignUpParams
import us.docbee.docbeeapp.domain.models.user.UserProfile

fun UserProfile.toMap(): Map<Any?, *> = mapOf(
    "uid" to uid,
    "name" to name,
    "lastname" to lastname,
    "email" to email,
    "dateOfBirth" to dateOfBirth,
    "phone" to phoneNumber,
    "createdAt" to createdAt
)

fun SignUpParams.toUserProfile(uid: String, createdAd: Long): UserProfile = UserProfile(
    uid = uid,
    name = name,
    lastname = lastName,
    email = email,
    dateOfBirth = dateOfBirth,
    phoneNumber = phoneNumber,
    createdAt = createdAd
)