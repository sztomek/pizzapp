package hu.sztomek.pizzapp.data.api

import hu.sztomek.pizzapp.data.model.FriendWebModel
import hu.sztomek.pizzapp.data.model.ListPlacesWebResponse
import hu.sztomek.pizzapp.data.model.PlaceWebModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface WebApi {

    @GET("pizza")
    fun listPlaces(): Single<ListPlacesWebResponse>

    @GET("pizza/{id}")
    fun placeDetails(@Path("id") id: String): Single<PlaceWebModel>

    @GET("friends")
    fun listFriends(): Single<List<FriendWebModel>>

}