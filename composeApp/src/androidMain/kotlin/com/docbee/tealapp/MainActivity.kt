package com.docbee.tealapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.docbee.tealapp.data.EmergencyRepository
import com.docbee.tealapp.service.createAndroidLocationService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

class MainActivity : ComponentActivity() {
    
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Fine location granted
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Coarse location granted
            }
            else -> {
                // No location access granted
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Initialize Firebase
        Firebase.initialize(this)
        
        // Configure Firebase to use local emulator for development
        dev.gitlive.firebase.functions.functions.useEmulator("10.0.2.2", 5001)
        dev.gitlive.firebase.auth.auth.useEmulator("10.0.2.2", 9099)
        dev.gitlive.firebase.firestore.firestore.useEmulator("10.0.2.2", 8080)
        android.util.Log.d("MainActivity", "Using Firebase emulators")
        
        // Initialize app dependencies
        AppDependencies.emergencyRepository = EmergencyRepository()
        AppDependencies.locationService = createAndroidLocationService(this)
        
        // Sign in anonymously for testing - do this BEFORE setContent
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
            try {
                android.util.Log.d("MainActivity", "Attempting anonymous sign-in...")
                val result = dev.gitlive.firebase.auth.auth.signInAnonymously()
                android.util.Log.d("MainActivity", "Signed in anonymously: ${result.user?.uid}")
                
                // Create test user profile in Firestore
                val userId = result.user?.uid ?: return@launch
                val testProfile = mapOf(
                    "displayName" to "Test User",
                    "email" to "test@docbee.com",
                    "emergencyContacts" to listOf(
                        mapOf(
                            "name" to "Emergency Contact",
                            "phoneNumber" to "+15551234567",
                            "relationship" to "Friend"
                        )
                    )
                )
                dev.gitlive.firebase.firestore.firestore
                    .collection("users")
                    .document(userId)
                    .set(testProfile)
                android.util.Log.d("MainActivity", "Created test user profile")
            } catch (e: Exception) {
                android.util.Log.e("MainActivity", "Setup failed: ${e.message}", e)
            }
        }
        
        // Request location permissions
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
