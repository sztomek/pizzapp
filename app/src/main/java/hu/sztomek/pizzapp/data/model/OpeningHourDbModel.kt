package hu.sztomek.pizzapp.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
class OpeningHourDbModel {
    @Id var id: Long = 0L

    var rawOpening: String = ""

    var place: ToOne<PlaceDbModel> = ToOne(this, OpeningHourDbModel_.place)
}