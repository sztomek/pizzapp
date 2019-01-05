package hu.sztomek.pizzapp.data

import hu.sztomek.pizzapp.data.api.DatabaseApi
import hu.sztomek.pizzapp.data.api.WebApi
import hu.sztomek.pizzapp.data.converter.toDomain
import hu.sztomek.pizzapp.data.model.ListPlacesWebResponse
import hu.sztomek.pizzapp.data.model.PlaceWebModel
import hu.sztomek.pizzapp.domain.Repository
import hu.sztomek.pizzapp.domain.error.DomainException
import hu.sztomek.pizzapp.domain.model.Friend
import hu.sztomek.pizzapp.domain.model.Place
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleTransformer
import java.io.IOException

class RepositoryImpl(private val databaseApi: DatabaseApi, private val webApi: WebApi) : Repository {

    private val DUMMY_RESPONSE = Single.just<ListPlacesWebResponse>(
        ListPlacesWebResponse().apply {
            list = ListPlacesWebResponse.Places().apply {
                places = listOf(
                    PlaceWebModel().apply {
                        id = "1"
                        name = "De Italian"
                        phone = "+31 20 583 6854"
                        website = "http://www.google.com"
                        city = "Amsterdam"
                        openingHours = listOf("Friday: 5:30 – 10:00 PM\"")
                        latitude = 52.36549829999
                        longitude = 4.87639139999
                        images = listOf(PlaceWebModel.Image().apply {
                            id = "14ceafa9-ad8e-4426-9f29-e452fd06e70f"
                            url = "https://www.culy.nl/wp-content/uploads/2014/05/Kebec-restaurant.jpg"
                            caption = "<a href=\"https://maps.google.com/maps/contrib/104965754628783390085/photos\">B M C van Weeren</a>"
                            expiration = "2018-11-23T00:20:44Z"
                        })
                        friendIds = listOf("4", "6", "7")
                    }
                )
            }
        }
    )

    override fun listPlaces(): Flowable<List<Place>> {
        return webApi.listPlaces()
            .compose(addErrorHandler())
            .map { it.list?.places }
            .map { it.map { it.toDomain() } }
            .toFlowable()
    }

    override fun listFriends(): Flowable<List<Friend>> {
        return webApi.listFriends()
            .compose(addErrorHandler())
            .map { it.map { it.toDomain() } }
            .toFlowable()
    }

    override fun getPlace(id: String): Flowable<Place> {
        return webApi.placeDetails(id)
            .compose(addErrorHandler())
            .map { it.toDomain() }
            .toFlowable()
    }

    private fun <I> addErrorHandler(): SingleTransformer<I, I> {
        return SingleTransformer { upstream ->
            upstream.onErrorResumeNext {
                val domainException = when (it) {
                    is IOException -> DomainException.NetworkError(it.message)
                    else -> DomainException.UnknownError(it.message)
                }
                Single.error(domainException)
            }
        }
    }
}