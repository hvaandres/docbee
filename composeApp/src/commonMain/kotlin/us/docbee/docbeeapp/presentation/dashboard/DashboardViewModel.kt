package us.docbee.docbeeapp.presentation.dashboard

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.presentation.components.toolbar.fetchToolbarContent
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.dashboard.effects.DashboardEffects
import us.docbee.docbeeapp.presentation.dashboard.events.DashboardEvents
import us.docbee.docbeeapp.presentation.dashboard.navigation.MenuBarItem
import us.docbee.docbeeapp.presentation.dashboard.states.StateUi
import us.docbee.docbeeapp.utils.ui.network.NetState
import us.docbee.docbeeapp.utils.ui.network.NetworkUtils

class DashboardViewModel(
    private val networkUtils: NetworkUtils,
): BaseViewModel<StateUi, DashboardEvents, DashboardEffects>(StateUi()) {

    init {
        onEvent(DashboardEvents.OnRegisterNetworkMonitor)
    }

    override fun onEvent(event: DashboardEvents) {
        when (event) {
            is DashboardEvents.OnRegisterNetworkMonitor -> observeNetworkStatus()
            is DashboardEvents.OnTabSelected -> onTabSelect(event.tab)
            is DashboardEvents.OnUpdateTab -> onUpdateTab(event.tab)
            is DashboardEvents.OnBackClicked -> emitEffect(DashboardEffects.NavigateBack)
        }
    }

    private fun onTabSelect(newTab: MenuBarItem) {
        val currentTab = uiState.value.tabItemSelected
        if (currentTab != newTab) {
            updateState {
                copy(tabItemSelected = newTab, toolbarSelected = fetchToolbarContent(newTab))
            }
            emitEffect(DashboardEffects.NavigateNewTab(newTab.route))
        }
    }

    private fun onUpdateTab(tab: MenuBarItem) {
        updateState { copy(tabItemSelected = tab, toolbarSelected = fetchToolbarContent(tab)) }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkUtils.isNetworkAvailable.collect { hasNetwork ->
                updateState { copy(hasNoConnection = hasNetwork != NetState.Connected) }
            }
        }
    }
}
