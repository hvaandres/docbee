package us.docbee.docbeeapp.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.compose_multiplatform
import docbee.composeapp.generated.resources.general_label_or
import docbee.composeapp.generated.resources.ic_apple
import docbee.composeapp.generated.resources.ic_google
import docbee.composeapp.generated.resources.login_description_label
import docbee.composeapp.generated.resources.login_form_apple_login
import docbee.composeapp.generated.resources.login_form_google_login
import docbee.composeapp.generated.resources.login_header_label
import docbee.composeapp.generated.resources.login_tabs_login_label
import docbee.composeapp.generated.resources.login_tabs_signup_label
import docbee.composeapp.generated.resources.login_title_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.navigation.DashboardRoute
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.Black100
import us.docbee.docbeeapp.presentation.theme.Blue100
import us.docbee.docbeeapp.presentation.theme.Gray
import us.docbee.docbeeapp.presentation.theme.Gray100
import us.docbee.docbeeapp.presentation.theme.Gray200
import us.docbee.docbeeapp.presentation.theme.White

@Composable
fun AuthScreen(navController: NavController) {
    var isLoginTabSelected by remember { mutableStateOf(true) }
    var snackbarHostState = remember { SnackbarHostState() }

    Box {
        AuthScreenContent(
            isLoginTab = isLoginTabSelected,
            onTabClicked = { isLoginTab -> isLoginTabSelected = isLoginTab },
            snackbarState = snackbarHostState,
            onAuthenticationSuccess = { navController.navigate(DashboardRoute) }
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
        )
    }
}

@Composable
fun AuthScreenContent(
    isLoginTab: Boolean,
    onTabClicked: (isLoginSelected: Boolean) -> Unit,
    snackbarState: SnackbarHostState,
    onAuthenticationSuccess: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginHeader(
            modifier = Modifier.fillMaxWidth()
                .background(
                    color = Black,
                    shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                )
                .padding(vertical = 74.dp)
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.login_title_label),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Black100
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.login_description_label),
                style = MaterialTheme.typography.bodySmall,
                color = Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoginTabs(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 42.dp)
                    .background(color = Gray100, shape = RoundedCornerShape(8.dp))
                    .padding(4.dp),
                isLoginTab = isLoginTab,
                onTabClicked = onTabClicked
            )
            Spacer(modifier = Modifier.height(30.dp))
            LoginScreen(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 42.dp),
                snackbarState = snackbarState,
                isLoginTabbed = isLoginTab,
                onAuthenticationSuccess = onAuthenticationSuccess
            )
            SignupScreen(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 42.dp),
                snackbarState = snackbarState,
                isSignupTabbed = !isLoginTab,
                onAuthenticationSuccess = onAuthenticationSuccess
            )
            LoginSocialButtons(modifier = Modifier.fillMaxWidth().padding(horizontal = 42.dp))
        }
    }
}

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(209.dp),
            imageVector = vectorResource(Res.drawable.compose_multiplatform),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(Res.string.login_header_label),
            color = White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun LoginTabs(modifier: Modifier = Modifier, isLoginTab: Boolean, onTabClicked: (Boolean) -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        ButtonTab(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.login_tabs_login_label),
            isSelected = isLoginTab,
            onClick = { onTabClicked(true) }
        )
        ButtonTab(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.login_tabs_signup_label),
            isSelected = !isLoginTab,
            onClick = { onTabClicked(false) }
        )
    }
}

@Composable
fun LoginSocialButtons(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.padding(horizontal = 38.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), thickness = 2.dp, color = Black)
            Text(
                text = stringResource(Res.string.general_label_or),
                style = MaterialTheme.typography.bodySmall,
                color = Gray
            )
            HorizontalDivider(modifier = Modifier.weight(1f), thickness = 2.dp, color = Black)
        }
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = stringResource(Res.string.login_form_apple_login),
            icon = vectorResource(Res.drawable.ic_apple)
        )
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(Res.string.login_form_google_login),
            icon = vectorResource(Res.drawable.ic_google)
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ButtonTab(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.height(32.dp)
            .background(
                color = if (isSelected) White else Gray100,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(interactionSource = null, onClick = onClick, indication = null),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) Blue100 else Gray200
        )
    }
}
