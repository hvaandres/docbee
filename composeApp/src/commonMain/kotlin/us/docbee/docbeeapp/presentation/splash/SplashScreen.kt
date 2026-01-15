package us.docbee.docbeeapp.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.ic_logo
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import us.docbee.docbeeapp.presentation.navigation.AuthenticationRoute
import us.docbee.docbeeapp.presentation.navigation.DashboardRoute
import us.docbee.docbeeapp.presentation.splash.effects.SplashEffects
import us.docbee.docbeeapp.presentation.theme.Black

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            val route = when (effect) {
                is SplashEffects.NavigateToAuthenticate -> AuthenticationRoute
                is SplashEffects.NavigateToDashboard -> DashboardRoute
            }
            navController.navigate(route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Black)
            .padding(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(Res.drawable.ic_logo),
            contentDescription = null
        )
    }
}