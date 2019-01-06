package hu.sztomek.pizzapp.data.model

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class PlaceDbModel
{

    @Id var id: Long = 0L
    var externalId: String = ""
    var name: String = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var imageUrl: String = ""
    var fetched: Long = 0L

    @Backlink(to = "place")
    var openingHours: List<OpeningHourDbModel> = emptyList()

}