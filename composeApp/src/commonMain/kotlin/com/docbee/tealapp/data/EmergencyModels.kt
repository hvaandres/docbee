package com.docbee.tealapp.data

import kotlinx.serialization.Serializable

@Serializable
data class EmergencyContact(
    val name: String,
    val phoneNumber: String, // E.164 format: +1234567890
    val relationship: String? = null
)

@Serializable
data class UserProfile(
    val displayName: String,
    val email: String,
    val phoneNumber: String? = null,
    val emergencyContacts: List<EmergencyContact> = emptyList()
)

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
) {
    fun toGoogleMapsUrl(): String {
        return "https://maps.google.com/?q=$latitude,$longitude"
    }
}

@Serializable
data class EmergencyAlertRequest(
    val location: Location,
    val userName: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class EmergencyAlertResponse(
    val success: Boolean,
    val message: String,
    val successCount: Int,
    val failureCount: Int,
    val details: List<ContactResult>
)

@Serializable
data class ContactResult(
    val success: Boolean,
    val contact: String,
    val messageId: String? = null,
    val error: String? = null
)

@Serializable
data class RateLimitResponse(
    val allowed: Boolean,
    val minutesRemaining: Int? = null,
    val message: String? = null
)
