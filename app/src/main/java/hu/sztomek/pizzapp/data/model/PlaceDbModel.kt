package hu.sztomek.pizzapp.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class PlaceDbModel(
    @Id var id: Long = 0L,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val url: String,
    val fetched: Long
)