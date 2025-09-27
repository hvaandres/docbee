package us.docbee.docbeeapp.domain.models.directory

sealed class DeleteContactResult {
    data object Success: DeleteContactResult()
    data object Error: DeleteContactResult()
    data object Unauthorized: DeleteContactResult()
}