package us.docbee.docbeeapp.utils.ui.managers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ClickCounterManager(
    private val requiredClicks: Int,
    private val resetTimeoutMillis: Long = 2_000L
) {
    private var clickCount = 0
    private var resetJob: Job? = null


    fun onClick(
        scope: CoroutineScope,
        onThresholdReached: () -> Unit,
        onCountChanged: (Int) -> Unit
    ) {
        clickCount++
        onCountChanged(clickCount)

        resetJob?.cancel()
        resetJob = scope.launch {
            delay(resetTimeoutMillis)
            reset()
            onCountChanged(clickCount)
        }

        if (clickCount >= requiredClicks) {
            reset()
            onThresholdReached()
            onCountChanged(clickCount)
        }
    }

    private fun reset() {
        clickCount = 0
        resetJob?.cancel()
        resetJob = null
    }
}