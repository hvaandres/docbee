package us.docbee.docbeeapp.utils.ui.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

actual class NetworkUtils actual constructor(context: Any?) {

    private val appContext = context as? Context
    private val connectivityManager =
        appContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _netState = MutableStateFlow(NetState.Unknown)

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) = update()
        override fun onLost(network: Network) = update()
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) = update()
    }

    // wip
    actual val netState: StateFlow<NetState>
        get() = _netState.asStateFlow()

    actual fun start() {
        connectivityManager.registerDefaultNetworkCallback(callback)
        update()
    }

    actual suspend fun checkAvailableNetwork(timeout: Long): Boolean {
        val step = 50L
        var waited = 0L
        while(_netState.value == NetState.Unknown && waited < timeout) {
            delay(step)
            waited += step
        }
        return _netState.value == NetState.Connected
    }

    private fun update() {
        val network = connectivityManager.activeNetwork
        val caps = network?.let { connectivityManager.getNetworkCapabilities(network) }

        val validated =
            caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        _netState.value = if (validated) NetState.Connected else NetState.Disconnected
    }
}