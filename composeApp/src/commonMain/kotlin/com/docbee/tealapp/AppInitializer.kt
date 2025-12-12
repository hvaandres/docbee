package com.docbee.tealapp

import com.docbee.tealapp.data.EmergencyRepository
import com.docbee.tealapp.service.LocationService
import com.docbee.tealapp.viewmodel.EmergencyViewModel

/**
 * Holds app-level dependencies
 * In a production app, you'd use a proper DI framework
 */
object AppDependencies {
    lateinit var emergencyRepository: EmergencyRepository
    lateinit var locationService: LocationService
    
    fun createEmergencyViewModel(): EmergencyViewModel {
        return EmergencyViewModel(
            repository = emergencyRepository,
            locationService = locationService
        )
    }
}
