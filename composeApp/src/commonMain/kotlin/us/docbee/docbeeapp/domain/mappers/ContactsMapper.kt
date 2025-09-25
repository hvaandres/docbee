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

fun Any?.toMutableStringMap(): MutableMap<String, Any> {
    val dictionary = this as? Map<*, *> ?: return mutableMapOf()
    return dictionary.entries.mapNotNull { (key, value) ->
        (key as? String)?.let { it to (value ?: "") }
    }.toMap(mutableMapOf())
}

fun MutableMap<String, Any>.toContactDomain() = ContactModel(
    uid = this["uid"] as String,
    firstName = this["firstName"] as String,
    lastName = this["lastName"] as String,
    email = this["email"] as String,
    phoneNumber = this["phoneNumber"] as String,
    gender = this["gender"] as String,
    address = this["address"] as String
)

fun ContactParams.toContactModel(): ContactModel = ContactModel(
    firstName = name,
    lastName = lastName,
    email = email,
    dateOfBirth = dateOfBirth,
    phoneNumber = phoneNumber,
    address = address,
)