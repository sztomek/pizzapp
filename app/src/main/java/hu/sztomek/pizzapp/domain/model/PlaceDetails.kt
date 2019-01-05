package hu.sztomek.pizzapp.domain.model

data class PlaceDetails(
    val id: String,
    val name: String,
    val rating: Float,
    val ratingCount: Int,
    val bookmarked: Boolean,
    val imageUrl: String,
    val friends: List<Friend> = emptyList(),
    val userRating: Int? = null
)