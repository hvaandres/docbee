package us.docbee.docbeeapp.data.datasources

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.data.entities.alerts.AlertFetchResponse
import us.docbee.docbeeapp.data.entities.alerts.AlertsDeleteResponse
import us.docbee.docbeeapp.domain.mappers.toMap
import us.docbee.docbeeapp.domain.mappers.toMutableStringMap
import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_ALERTS
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_USER
import us.docbee.docbeeapp.wrappers.AlertRemoteStorage

@OptIn(ExperimentalForeignApi::class)
class IosAlertsRemoteDataSource : AlertsRemoteDataSource {

    private val remote = AlertRemoteStorage()

    override suspend fun saveAlert(uid: String, alert: AlertModel) {
        suspendCancellableCoroutine<Unit> { thread ->
            remote.saveAlertWithCollection(
                collection = FIRESTORE_COLLECTION_USER,
                collectionAlert = FIRESTORE_COLLECTION_ALERTS,
                uid = uid,
                alert = alert.toMap()
            ) { error ->
                thread.resume(Unit, null)
            }
        }
    }

    override suspend fun fetchAlert(uid: String): AlertFetchResponse {
        return suspendCancellableCoroutine { thread ->
            remote.fetchAlertWithCollection(
                collection = FIRESTORE_COLLECTION_USER,
                collectionAlert = FIRESTORE_COLLECTION_ALERTS,
                uid = uid
            ) { alerts, error ->
                val alertResponse = if (alerts != null) {
                    AlertFetchResponse(data = alerts.map { it.toMutableStringMap() })
                } else {
                    val errorCode = error?.userInfo?.get("FIRAuthErrorUserInfoNameKey") as? String
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    AlertFetchResponse(errorCode = errorCode, errorMessage = errorMessage)
                }
                thread.resume(alertResponse, null)
            }
        }
    }

    override suspend fun deleteAlert(uid: String, alertUid: String): AlertsDeleteResponse {
        return suspendCancellableCoroutine { thread ->
            remote.deleteAlertWithCollection(
                collection = FIRESTORE_COLLECTION_USER,
                collectionAlert = FIRESTORE_COLLECTION_ALERTS,
                uid = uid,
                alertUid = alertUid
            ) { success, error ->
                val deleteResponse = if (success) {
                    AlertsDeleteResponse(success)
                } else {
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    AlertsDeleteResponse(errorCode = "UNKNOWN_ERROR", errorMessage = errorMessage)
                }
                thread.resume(deleteResponse, null)
            }
        }
    }
}

actual fun getAlertsDataSource(): AlertsRemoteDataSource = IosAlertsRemoteDataSource()