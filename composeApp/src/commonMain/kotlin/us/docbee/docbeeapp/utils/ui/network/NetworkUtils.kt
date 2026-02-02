package us.docbee.docbeeapp.utils.ui.network

import kotlinx.coroutines.flow.StateFlow

expect class NetworkUtils(context: Any?) {

    val netState: StateFlow<NetState>
    fun start()
    suspend fun checkAvailableNetwork(timeout: Long = 1200): Boolean
}
