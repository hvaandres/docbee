package us.docbee.docbeeapp.utils.ui.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_queue_create

actual class NetworkUtils actual constructor(context: Any?) {

    private val _netState = MutableStateFlow(NetState.Unknown)
    private val monitor = nw_path_monitor_create()
    private val queue = dispatch_queue_create("us.docbee.network.monitor", null)

    actual val netState: StateFlow<NetState>
        get() = _netState.asStateFlow()


    actual fun start() {
        nw_path_monitor_set_update_handler(monitor) { path ->
            val isStatusSatisfied = nw_path_get_status(path) == nw_path_status_satisfied
            _netState.value = if (isStatusSatisfied) NetState.Connected else NetState.Disconnected
        }
        nw_path_monitor_set_queue(monitor, queue)
        nw_path_monitor_start(monitor)
    }

    actual suspend fun checkAvailableNetwork(timeout: Long): Boolean {
        val step = 50L
        var waited = 0L
        while (_netState.value == NetState.Unknown && waited < timeout) {
            delay(step)
            waited += step
        }
        return _netState.value == NetState.Connected
    }
}