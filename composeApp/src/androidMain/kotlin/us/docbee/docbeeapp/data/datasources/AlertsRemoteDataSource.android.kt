package us.docbee.docbeeapp.data.datasources

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import us.docbee.docbeeapp.data.entities.alerts.AlertFetchResponse
import us.docbee.docbeeapp.data.entities.alerts.AlertSaveResponse
import us.docbee.docbeeapp.data.entities.alerts.AlertsDeleteResponse
import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_ALERTS
import us.docbee.docbeeapp.utils.FIRESTORE_COLLECTION_USER

class AndroidAlertsRemoteDataSource : AlertsRemoteDataSource {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun saveAlert(uid: String, alert: AlertModel): AlertSaveResponse {
        return try {
            val reference = db
                .collection(FIRESTORE_COLLECTION_USER)
                .document(uid)
                .collection(FIRESTORE_COLLECTION_ALERTS)
                .document()

            alert.uid = reference.id

            reference
                .set(alert)
                .await()

            AlertSaveResponse(alert = alert)
        } catch (ex: Exception) {
            AlertSaveResponse(errorCode = "UNKNOWN_ERROR", errorMessage = ex.localizedMessage)
        }
    }

    override suspend fun fetchAlert(uid: String): AlertFetchResponse {
        return try {
            val query = db.collection(FIRESTORE_COLLECTION_USER)
                .document(uid)
                .collection(FIRESTORE_COLLECTION_ALERTS)
                .get()
                .await()

            AlertFetchResponse(data = query.documents.map { it.data })

        } catch (ex: Exception) {
            AlertFetchResponse(errorCode = "UNKNOWN_ERROR", errorMessage = ex.localizedMessage)
        }
    }

    override suspend fun deleteAlert(uid: String, alertUid: String): AlertsDeleteResponse {
        return try {
            db.collection(FIRESTORE_COLLECTION_USER)
                .document(uid)
                .collection(FIRESTORE_COLLECTION_ALERTS)
                .document(alertUid)
                .delete()

            AlertsDeleteResponse(successDeleted = true)
        } catch (ex: Exception) {
            AlertsDeleteResponse(errorCode = "UNKNOWN_ERROR", errorMessage = ex.localizedMessage)
        }
    }
}

actual fun getAlertsDataSource(): AlertsRemoteDataSource = AndroidAlertsRemoteDataSource()