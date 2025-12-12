package com.docbee.tealapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docbee.tealapp.data.EmergencyRepository
import com.docbee.tealapp.data.Location
import com.docbee.tealapp.service.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class EmergencyUiState {
    data object Idle : EmergencyUiState()
    data object Loading : EmergencyUiState()
    data class Success(val message: String, val contactsNotified: Int) : EmergencyUiState()
    data class Error(val message: String) : EmergencyUiState()
    data class RateLimited(val minutesRemaining: Int) : EmergencyUiState()
}

class EmergencyViewModel(
    private val repository: EmergencyRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _uiState = MutableStateFlow<EmergencyUiState>(EmergencyUiState.Idle)
    val uiState: StateFlow<EmergencyUiState> = _uiState.asStateFlow()

    private val _hasLocationPermission = MutableStateFlow(false)
    val hasLocationPermission: StateFlow<Boolean> = _hasLocationPermission.asStateFlow()

    init {
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        _hasLocationPermission.value = locationService.hasPermissions()
    }

    fun requestLocationPermission() {
        viewModelScope.launch {
            val granted = locationService.requestPermissions()
            _hasLocationPermission.value = granted
        }
    }

    fun sendEmergencyAlert() {
        viewModelScope.launch {
            println("[EmergencyViewModel] sendEmergencyAlert called")
            _uiState.value = EmergencyUiState.Loading
            
            // Check if user is authenticated
            if (!repository.isUserAuthenticated()) {
                println("[EmergencyViewModel] User not authenticated!")
                _uiState.value = EmergencyUiState.Error(
                    "Please sign in to send emergency alerts"
                )
                return@launch
            }
            
            // Check if user has emergency contacts
            println("[EmergencyViewModel] Checking if user has emergency contacts...")
            val hasContactsResult = repository.hasEmergencyContacts()
            hasContactsResult.fold(
                onSuccess = { hasContacts ->
                    if (!hasContacts) {
                        println("[EmergencyViewModel] ⚠️ User has no emergency contacts configured")
                        _uiState.value = EmergencyUiState.Error(
                            "At this time you don't have any contacts listed. Please add your contacts."
                        )
                        return@launch
                    }
                    println("[EmergencyViewModel] User has emergency contacts - proceeding...")
                },
                onFailure = { error ->
                    println("[EmergencyViewModel] Error checking contacts: ${error.message}")
                    _uiState.value = EmergencyUiState.Error(
                        "Failed to check emergency contacts: ${error.message}"
                    )
                    return@launch
                }
            )

            // Check rate limit
            println("[EmergencyViewModel] Checking rate limit...")
            val rateLimitResult = repository.checkRateLimit()
            rateLimitResult.fold(
                onSuccess = { rateLimitResponse ->
                    println("[EmergencyViewModel] Rate limit check passed: ${rateLimitResponse.allowed}")
                    if (!rateLimitResponse.allowed) {
                        _uiState.value = EmergencyUiState.RateLimited(
                            rateLimitResponse.minutesRemaining ?: 0
                        )
                        return@launch
                    }

                    // Get current location
                    println("[EmergencyViewModel] Getting location...")
                    val locationResult = locationService.getCurrentLocation()
                    locationResult.fold(
                        onSuccess = { location ->
                            println("[EmergencyViewModel] Got location: $location")
                            sendAlert(location)
                        },
                        onFailure = { error ->
                            println("[EmergencyViewModel] Location error: ${error.message}")
                            _uiState.value = EmergencyUiState.Error(
                                error.message ?: "Failed to get location"
                            )
                        }
                    )
                },
                onFailure = { error ->
                    println("[EmergencyViewModel] Rate limit error: ${error.message}")
                    _uiState.value = EmergencyUiState.Error(
                        error.message ?: "Failed to check rate limit"
                    )
                }
            )
        }
    }

    private suspend fun sendAlert(location: Location) {
        val userName = repository.getCurrentUserName() ?: "User"
        
        val result = repository.sendEmergencyAlert(
            location = location,
            userName = userName
        )

        result.fold(
            onSuccess = { response ->
                if (response.success) {
                    _uiState.value = EmergencyUiState.Success(
                        message = response.message,
                        contactsNotified = response.successCount
                    )
                } else {
                    _uiState.value = EmergencyUiState.Error(
                        "Failed to send emergency alert"
                    )
                }
            },
            onFailure = { error ->
                _uiState.value = EmergencyUiState.Error(
                    error.message ?: "An error occurred"
                )
            }
        )
    }

    fun resetState() {
        _uiState.value = EmergencyUiState.Idle
    }
}
