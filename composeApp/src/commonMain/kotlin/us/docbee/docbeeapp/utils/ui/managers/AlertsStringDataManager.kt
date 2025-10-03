package us.docbee.docbeeapp.utils.ui.managers

import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.alerts_item_accident_description
import docbee.composeapp.generated.resources.alerts_item_accident_title
import docbee.composeapp.generated.resources.alerts_item_dizzy_description
import docbee.composeapp.generated.resources.alerts_item_dizzy_title
import docbee.composeapp.generated.resources.alerts_item_fall_description
import docbee.composeapp.generated.resources.alerts_item_fall_title
import docbee.composeapp.generated.resources.alerts_item_lost_description
import docbee.composeapp.generated.resources.alerts_item_lost_title
import org.jetbrains.compose.resources.getString
import us.docbee.docbeeapp.domain.managers.AlertsStringsManager

class AlertsStringDataManager : AlertsStringsManager {
    companion object {
        const val IC_CONFUSED = "ic_confused"
        const val IC_FALLING = "ic_falling"
        const val IC_DIZZY = "ic_dizzy"
        const val IC_ACCIDENT = "ic_accident"

        const val UID_LOST = "1"
        const val UID_FALLING = "2"
        const val UID_DIZZY = "3"
        const val UID_ACCIDENT = "4"
    }

    override suspend fun getLostUid(): String = UID_LOST

    override suspend fun getLostTitle(): String =
        getString(Res.string.alerts_item_lost_title)

    override suspend fun getLostDescription(): String =
        getString(Res.string.alerts_item_lost_description)

    override suspend fun getLostIcon(): String = IC_CONFUSED

    override suspend fun getFallingUid(): String = UID_FALLING

    override suspend fun getFallingTitle(): String =
        getString(Res.string.alerts_item_fall_title)

    override suspend fun getFallingDescription(): String =
        getString(Res.string.alerts_item_fall_description)

    override suspend fun getFallingIcon(): String = IC_FALLING

    override suspend fun getDizzyUid(): String = UID_DIZZY

    override suspend fun getDizzyTitle(): String =
        getString(Res.string.alerts_item_dizzy_title)

    override suspend fun getDizzyDescription(): String =
        getString(Res.string.alerts_item_dizzy_description)

    override suspend fun getDizzyIcon(): String = IC_DIZZY

    override suspend fun getAccidentUid(): String = UID_ACCIDENT

    override suspend fun getAccidentTitle(): String =
        getString(Res.string.alerts_item_accident_title)

    override suspend fun getAccidentDescription(): String =
        getString(Res.string.alerts_item_accident_description)

    override suspend fun getAccidentIcon(): String = IC_ACCIDENT
}