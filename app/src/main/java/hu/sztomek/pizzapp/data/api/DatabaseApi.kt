package hu.sztomek.pizzapp.data.api

import hu.sztomek.pizzapp.data.model.PlaceDbModel
import io.reactivex.Completable
import io.reactivex.Flowable

interface DatabaseApi {

    fun listPlaces(): Flowable<List<PlaceDbModel>>
    fun storePlaces(places: Map<PlaceDbModel, List<String>>): Completable
}