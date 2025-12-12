package com.docbee.tealapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.docbee.tealapp.viewmodel.EmergencyUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

@Composable
fun EmergencyButton(
    uiState: EmergencyUiState,
    onEmergencyClick: () -> Unit,
    onDismissMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Triple-press detection state
    var pressCount by remember { mutableStateOf(0) }
    var resetJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()
    val resetDelayMs = 2000L // Reset counter after 2 seconds of inactivity
    
    // Reset counter when alert is being sent (Loading state)
    LaunchedEffect(uiState) {
        if (uiState is EmergencyUiState.Loading) {
            println("[EmergencyButton] Loading state detected - ensuring counter is reset")
            pressCount = 0
            resetJob?.cancel()
            resetJob = null
        }
    }
    
    // Handle button click with triple-press logic
    val handlePress = {
        println("[EmergencyButton] Button pressed! Current count: $pressCount")
        
        // Cancel any existing reset timer
        resetJob?.cancel()
        println("[EmergencyButton] Cancelled previous reset timer")
        
        pressCount++
        println("[EmergencyButton] Press count now: $pressCount")
        
        if (pressCount >= 3) {
            println("[EmergencyButton] Triple press detected! Triggering emergency alert.")
            pressCount = 0  // Reset counter first
            resetJob = null  // Clear the job reference
            onEmergencyClick()  // Trigger the emergency alert
            println("[EmergencyButton] Emergency alert triggered and counter reset")
        } else {
            println("[EmergencyButton] Press $pressCount/3 - waiting for more presses...")
            // Start a new reset timer
            resetJob = scope.launch {
                println("[EmergencyButton] Reset timer started (${resetDelayMs}ms)")
                delay(resetDelayMs)
                println("[EmergencyButton] Reset timeout reached - resetting counter from $pressCount to 0")
                pressCount = 0
                resetJob = null
            }
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Status message
        AnimatedVisibility(
            visible = uiState !is EmergencyUiState.Idle,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            StatusCard(
                uiState = uiState,
                onDismiss = onDismissMessage
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Emergency button
        EmergencyButtonCircle(
            isLoading = uiState is EmergencyUiState.Loading,
            onClick = handlePress,
            enabled = uiState !is EmergencyUiState.Loading,
            pressCount = pressCount
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (pressCount > 0) "Press ${3 - pressCount} more time(s)" else "Press 3 times for Emergency",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun EmergencyButtonCircle(
    isLoading: Boolean,
    onClick: () -> Unit,
    enabled: Boolean,
    pressCount: Int = 0
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        )
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .size(200.dp)
            .then(if (!isLoading) Modifier else Modifier),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE53935), // Red emergency color
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(48.dp)
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SOS",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
                if (pressCount > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$pressCount/3",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    uiState: EmergencyUiState,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (uiState) {
                is EmergencyUiState.Success -> Color(0xFF4CAF50)
                is EmergencyUiState.Error -> Color(0xFFE53935)
                is EmergencyUiState.RateLimited -> Color(0xFFFF9800)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is EmergencyUiState.Success -> {
                    Text(
                        text = "✓ Alert Sent!",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                is EmergencyUiState.Error -> {
                    Text(
                        text = "⚠ Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                is EmergencyUiState.RateLimited -> {
                    Text(
                        text = "⏱ Rate Limited",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please wait ${uiState.minutesRemaining} more minute(s)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("Dismiss")
            }
        }
    }
}
