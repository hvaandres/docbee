package us.docbee.docbeeapp.data.datasources

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import us.docbee.docbeeapp.data.entities.alerts.AlertFetchResponse
import us.docbee.docbeeapp.data.entities.alerts.AlertSaveResponse
import us.docbee.docbeeapp.data.entities.alerts.AlertsDeleteResponse
import us.docbee.docbeeapp.domain.mappers.toMap
import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_ALERTS
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_USER
import us.docbee.docbeeapp.wrappers.AlertRemoteStorage

@OptIn(ExperimentalForeignApi::class)
class IosAlertsRemoteDataSource : AlertsRemoteDataSource {

    private val remote = AlertRemoteStorage()

    @Suppress("CAST_NEVER_SUCCEEDS")
    override suspend fun saveAlert(uid: String, alert: AlertModel): AlertSaveResponse {
        return suspendCancellableCoroutine { thread ->
            remote.saveAlertWithCollection(
                collection = FIRESTORE_COLLECTION_USER,
                collectionAlert = FIRESTORE_COLLECTION_ALERTS,
                uid = uid,
                alert = alert.toMap()
            ) { alert, error ->
                val saveResponse = if (alert != null) {
                    AlertSaveResponse(alert = alert as? AlertModel)
                } else {
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    AlertSaveResponse(errorCode = "UNKNOWN_ERROR", errorMessage = errorMessage)
                }
                thread.resume(saveResponse, null)
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
                    AlertFetchResponse(data = alerts.map { it as AlertModel })
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

    @Suppress("CAST_NEVER_SUCCEEDS")
    override suspend fun editAlert(
        uid: String,
        alert: AlertModel
    ): AlertSaveResponse {
        return suspendCancellableCoroutine { thread ->
            remote.modifyAlertWithCollection(
                collection = FIRESTORE_COLLECTION_USER,
                collectionAlert = FIRESTORE_COLLECTION_ALERTS,
                uid = uid,
                alert = alert.toMap()
            ) { alert, error ->
                val saveResponse = if (alert != null) {
                    AlertSaveResponse(alert = alert as? AlertModel)
                } else {
                    val errorMessage = error?.userInfo?.get("NSLocalizedDescription") as? String
                    AlertSaveResponse(errorCode = "UNKNOWN_ERROR", errorMessage = errorMessage)
                }
                thread.resume(saveResponse, null)
            }
        }
    }
}

actual fun getAlertsDataSource(): AlertsRemoteDataSource = IosAlertsRemoteDataSource()