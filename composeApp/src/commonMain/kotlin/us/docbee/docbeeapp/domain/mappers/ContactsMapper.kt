package us.docbee.docbeeapp.domain.mappers

import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.domain.models.signup.ContactParams

fun ContactModel.toMap(): Map<Any?, *> = mapOf(
    "firstName" to firstName,
    "lastName" to lastName,
    "email" to email,
    "phoneNumber" to phoneNumber,
    "gender" to gender,
    "address" to address,
    "uid" to uid
)

fun ContactParams.toContactModel(): ContactModel = ContactModel(
    firstName = name,
    lastName = lastName,
    email = email,
    dateOfBirth = dateOfBirth,
    phoneNumber = phoneNumber,
    address = address,
)