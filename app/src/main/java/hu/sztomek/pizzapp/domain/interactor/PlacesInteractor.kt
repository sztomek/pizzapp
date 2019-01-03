package hu.sztomek.pizzapp.domain.interactor

import hu.sztomek.pizzapp.domain.Repository
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.domain.model.Place
import io.reactivex.Flowable
import javax.inject.Inject

class PlacesInteractor @Inject constructor(private val repository: Repository) {

    fun listPlaces(): Flowable<List<Place>> {
        return repository.listPlaces()
    }

    fun getPlace(action: Action.GetPlaceDetails): Flowable<Place> {
        return repository.getPlace(action.id)
    }

}