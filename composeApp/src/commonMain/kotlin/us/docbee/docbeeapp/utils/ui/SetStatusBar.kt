package us.docbee.docbeeapp.utils.ui

import androidx.compose.runtime.Composable

@Composable
expect fun SetStatusBar(isDarkMode: Boolean = false)

@Composable
expect fun SetModalStatusBar(isDarkMode: Boolean = false)