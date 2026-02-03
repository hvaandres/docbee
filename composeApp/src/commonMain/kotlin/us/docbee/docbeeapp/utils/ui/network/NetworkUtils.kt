package us.docbee.docbeeapp.utils.ui.network

import kotlinx.coroutines.flow.StateFlow

interface NetworkUtils {

    val isNetworkAvailable: StateFlow<NetState>
    fun start()
}

expect fun getNetworkUtils(): NetworkUtils
