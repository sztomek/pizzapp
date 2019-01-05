package hu.sztomek.pizzapp.domain.common

sealed class Action {

    object ListPlaces: Action()
    data class GetPlaceDetails(val id: String): Action()
    data class BookmarkPlace(val id: String): Action()
    data class BookPlace(val id: String): Action()
    data class RatePlace(val id: String, val rating: Int): Action()
}