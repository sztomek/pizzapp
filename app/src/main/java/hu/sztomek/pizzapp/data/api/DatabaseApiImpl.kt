package hu.sztomek.pizzapp.data.api

import hu.sztomek.pizzapp.data.model.OpeningHourDbModel
import hu.sztomek.pizzapp.data.model.PlaceDbModel
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.rx.RxQuery
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable

class DatabaseApiImpl(boxStore: BoxStore) : DatabaseApi {

    private val placeBox: Box<PlaceDbModel> by lazy {
        boxStore.boxFor(PlaceDbModel::class.java)
    }

    private val openingHourBox: Box<OpeningHourDbModel> by lazy {
        boxStore.boxFor(OpeningHourDbModel::class.java)
    }

    override fun listPlaces(): Flowable<List<PlaceDbModel>> {
        val query = placeBox.query().build()

        return RxQuery.observable(query)
            .toFlowable(BackpressureStrategy.LATEST)
    }

    override fun storePlaces(places: Map<PlaceDbModel, List<String>>): Completable {
        return Completable.fromAction {
            places.entries.forEach { entry ->
                placeBox.put(entry.key)
                entry.value.map {
                    val openingHourDbModel = OpeningHourDbModel()
                    openingHourDbModel.rawOpening = it
                    openingHourDbModel.place.target = entry.key
                    openingHourDbModel
                }.forEach { openingHourBox.put(it) }
            }
        }
    }
}