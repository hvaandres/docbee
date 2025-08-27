package us.docbee.docbeeapp.presentation.directory.events

import us.docbee.docbeeapp.presentation.components.SwipeState

sealed class DirectoryEvents {
    data object OnInitEvent: DirectoryEvents()
    data class OnSearchEvent(val search: String): DirectoryEvents()
    data class OnSwipeContactEvent(val uid: String, val swipeState: SwipeState): DirectoryEvents()
    data class OnArchiveContactEvent(val uid: String): DirectoryEvents()
    data class OnDeleteContactEvent(val uid: String): DirectoryEvents()
    data class OnClickContactEvent(val uid: String): DirectoryEvents()
}