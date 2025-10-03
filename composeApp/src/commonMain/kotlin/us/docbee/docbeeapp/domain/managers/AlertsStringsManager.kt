package us.docbee.docbeeapp.domain.managers

interface AlertsStringsManager {
    suspend fun getLostUid(): String
    suspend fun getLostTitle(): String
    suspend fun getLostDescription(): String
    suspend fun getLostIcon(): String

    suspend fun getFallingUid(): String
    suspend fun getFallingTitle(): String
    suspend fun getFallingDescription(): String
    suspend fun getFallingIcon(): String

    suspend fun getDizzyUid(): String
    suspend fun getDizzyTitle(): String
    suspend fun getDizzyDescription(): String
    suspend fun getDizzyIcon(): String

    suspend fun getAccidentUid(): String
    suspend fun getAccidentTitle(): String
    suspend fun getAccidentDescription(): String
    suspend fun getAccidentIcon(): String
}