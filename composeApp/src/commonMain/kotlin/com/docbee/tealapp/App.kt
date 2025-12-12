package com.docbee.tealapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.docbee.tealapp.ui.EmergencyButton
import com.docbee.tealapp.viewmodel.EmergencyViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    // Note: In production, initialize AppDependencies in MainActivity/iOSApp
    val viewModel: EmergencyViewModel = try {
        viewModel { AppDependencies.createEmergencyViewModel() }
    } catch (e: Exception) {
        // Fallback for preview/testing without proper initialization
        return MaterialTheme {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    "Please initialize AppDependencies in MainActivity",
                    modifier = Modifier.padding(androidx.compose.ui.unit.dp(16.dp))
                )
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    
    // Track authentication status
    var isAuthenticated by remember { mutableStateOf(Firebase.auth.currentUser != null) }
    
    LaunchedEffect(Unit) {
        Firebase.auth.authStateChanged.collect { user ->
            isAuthenticated = user != null
            println("[App] Auth state changed: ${if (user != null) "authenticated (${user.uid})" else "not authenticated"}")
        }
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        androidx.compose.foundation.layout.Column {
                            Text("DocBee Emergency")
                            Text(
                                text = if (isAuthenticated) "✓ Authenticated" else "✗ Not Authenticated",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isAuthenticated) 
                                    androidx.compose.ui.graphics.Color(0xFF4CAF50) 
                                else 
                                    androidx.compose.ui.graphics.Color(0xFFE53935)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            EmergencyButton(
                uiState = uiState,
                onEmergencyClick = { viewModel.sendEmergencyAlert() },
                onDismissMessage = { viewModel.resetState() },
                modifier = Modifier
                    .padding(paddingValues)
                    .safeContentPadding()
            )
        }
    }
}
