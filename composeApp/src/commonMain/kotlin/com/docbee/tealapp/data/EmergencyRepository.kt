package com.docbee.tealapp.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.functions.functions

class EmergencyRepository {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val functions = Firebase.functions

    /**
     * Check if user has emergency contacts configured
     */
    suspend fun hasEmergencyContacts(): Result<Boolean> {
        return try {
            println("[EmergencyRepository] Checking if user has emergency contacts...")
            val userId = auth.currentUser?.uid
            if (userId == null) {
                println("[EmergencyRepository] No authenticated user")
                return Result.success(false)
            }
            
            val doc = firestore
                .collection("users")
                .document(userId)
                .get()
            
            if (!doc.exists) {
                println("[EmergencyRepository] User document does not exist")
                return Result.success(false)
            }
            
            val data = doc.data()
            val emergencyContacts = data["emergencyContacts"] as? List<*> ?: emptyList<Any>()
            val contactCount = emergencyContacts.size
            
            println("[EmergencyRepository] User has $contactCount emergency contact(s)")
            
            if (contactCount == 0) {
                println("[EmergencyRepository] ⚠️ No emergency contacts found for user")
            }
            
            Result.success(contactCount > 0)
        } catch (e: Exception) {
            println("[EmergencyRepository] Error checking emergency contacts: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * Check if the user can send an emergency alert (rate limiting)
     */
    suspend fun checkRateLimit(): Result<RateLimitResponse> {
        return try {
            println("[EmergencyRepository] Calling checkEmergencyRateLimit...")
            val result = functions
                .httpsCallable("checkEmergencyRateLimit")
                .invoke()
            
            println("[EmergencyRepository] Got result: ${result.data}")
            val data = result.data as Map<*, *>
            val response = RateLimitResponse(
                allowed = data["allowed"] as Boolean,
                minutesRemaining = data["minutesRemaining"] as? Int,
                message = data["message"] as? String
            )
            
            println("[EmergencyRepository] Rate limit check successful: allowed=${response.allowed}")
            Result.success(response)
        } catch (e: Exception) {
            println("[EmergencyRepository] Error checking rate limit: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Send emergency SMS to all user's contacts
     */
    suspend fun sendEmergencyAlert(
        location: Location,
        userName: String
    ): Result<EmergencyAlertResponse> {
        return try {
            println("[EmergencyRepository] sendEmergencyAlert called")
            println("[EmergencyRepository] Location: lat=${location.latitude}, lng=${location.longitude}")
            println("[EmergencyRepository] Username: $userName")
            
            val request = mapOf(
                "location" to mapOf(
                    "latitude" to location.latitude,
                    "longitude" to location.longitude
                ),
                "userName" to userName,
                "timestamp" to System.currentTimeMillis()
            )
            println("[EmergencyRepository] Request payload: $request")

            println("[EmergencyRepository] Calling sendEmergencySMS function...")
            val result = functions
                .httpsCallable("sendEmergencySMS")
                .invoke(request)
            
            println("[EmergencyRepository] Function call returned successfully")
            val data = result.data as Map<*, *>
            println("[EmergencyRepository] Response data: $data")
            val detailsList = (data["details"] as? List<*>)?.mapNotNull { detail ->
                val detailMap = detail as? Map<*, *>
                detailMap?.let {
                    ContactResult(
                        success = it["success"] as Boolean,
                        contact = it["contact"] as String,
                        messageId = it["messageId"] as? String,
                        error = it["error"] as? String
                    )
                }
            } ?: emptyList()

            val response = EmergencyAlertResponse(
                success = data["success"] as Boolean,
                message = data["message"] as String,
                successCount = (data["successCount"] as Number).toInt(),
                failureCount = (data["failureCount"] as Number).toInt(),
                details = detailsList
            )
            println("[EmergencyRepository] Alert sent successfully: ${response.successCount} succeeded, ${response.failureCount} failed")

            Result.success(response)
        } catch (e: Exception) {
            println("[EmergencyRepository] Error sending emergency alert: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }

    /**
     * Get current user's profile
     */
    suspend fun getUserProfile(): Result<UserProfile?> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.success(null)
            
            val doc = firestore
                .collection("users")
                .document(userId)
                .get()

            if (!doc.exists) {
                return Result.success(null)
            }

            val data = doc.data()
            val contactsList = (data["emergencyContacts"] as? List<*>)?.mapNotNull { contact ->
                val contactMap = contact as? Map<*, *>
                contactMap?.let {
                    EmergencyContact(
                        name = it["name"] as String,
                        phoneNumber = it["phoneNumber"] as String,
                        relationship = it["relationship"] as? String
                    )
                }
            } ?: emptyList()

            val profile = UserProfile(
                displayName = data["displayName"] as? String ?: "",
                email = data["email"] as? String ?: "",
                phoneNumber = data["phoneNumber"] as? String,
                emergencyContacts = contactsList
            )

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update user's profile with emergency contacts
     */
    suspend fun updateUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: throw Exception("User not authenticated")

            val data = mapOf(
                "displayName" to profile.displayName,
                "email" to profile.email,
                "phoneNumber" to profile.phoneNumber,
                "emergencyContacts" to profile.emergencyContacts.map { contact ->
                    mapOf(
                        "name" to contact.name,
                        "phoneNumber" to contact.phoneNumber,
                        "relationship" to contact.relationship
                    )
                }
            )

            firestore
                .collection("users")
                .document(userId)
                .set(data)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check if user is authenticated
     */
    fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Get current user display name
     */
    fun getCurrentUserName(): String? {
        return auth.currentUser?.displayName
    }
}
