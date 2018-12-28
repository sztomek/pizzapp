package hu.sztomek.pizzapp.domain.model

data class Place(val id: String,
                 val name: String,
                 val location: Location,
                 val thumbnail: String,
                 val open: Boolean)