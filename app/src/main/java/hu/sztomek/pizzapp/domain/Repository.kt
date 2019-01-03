package hu.sztomek.pizzapp.domain

import hu.sztomek.pizzapp.domain.model.Friend
import hu.sztomek.pizzapp.domain.model.Place
import io.reactivex.Flowable

interface Repository {

    fun listPlaces(): Flowable<List<Place>>

    fun listFriends(): Flowable<List<Friend>>

    fun getPlace(id: String): Flowable<Place>

}