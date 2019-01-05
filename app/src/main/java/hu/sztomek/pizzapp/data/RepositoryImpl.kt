package hu.sztomek.pizzapp.data

import hu.sztomek.pizzapp.data.api.DatabaseApi
import hu.sztomek.pizzapp.data.api.WebApi
import hu.sztomek.pizzapp.data.converter.toPlace
import hu.sztomek.pizzapp.data.converter.toDomain
import hu.sztomek.pizzapp.data.converter.toPlaceDetails
import hu.sztomek.pizzapp.domain.Repository
import hu.sztomek.pizzapp.domain.error.DomainException
import hu.sztomek.pizzapp.domain.model.Friend
import hu.sztomek.pizzapp.domain.model.Place
import hu.sztomek.pizzapp.domain.model.PlaceDetails
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleTransformer
import java.io.IOException

class RepositoryImpl(private val databaseApi: DatabaseApi, private val webApi: WebApi) : Repository {

    override fun listPlaces(): Flowable<List<Place>> {
        return webApi.listPlaces()
            .compose(addErrorHandler())
            .map { it.list?.places }
            .map { it.map { it.toPlace() } }
            .toFlowable()
    }

    override fun listFriends(): Flowable<List<Friend>> {
        return webApi.listFriends()
            .compose(addErrorHandler())
            .map { it.map { it.toDomain() } }
            .toFlowable()
    }

    override fun getPlace(id: String): Flowable<PlaceDetails> {
        return webApi.placeDetails(id)
            .compose(addErrorHandler())
            .flatMap { placeWebModel ->
                webApi.listFriends()
                    .compose(addErrorHandler())
                    .map { friendWebModels ->
                        friendWebModels.filter {
                            placeWebModel.friendIds.contains(it.id.toString())
                        }
                        .map { it.toDomain() }
                    }
                    .map { filteredFriends ->
                        placeWebModel.toPlaceDetails().copy(friends = filteredFriends)
                    }
            }
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