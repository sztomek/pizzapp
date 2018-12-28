package hu.sztomek.pizzapp.domain.common

sealed class Action {

    object ListPlaces: Action()
    data class GetPlaceDetails(val id: String): Action()
    object ListFriends: Action()
}