package hu.sztomek.pizzapp.data

import hu.sztomek.pizzapp.data.api.DatabaseApi
import hu.sztomek.pizzapp.data.api.WebApi
import hu.sztomek.pizzapp.data.converter.toDb
import hu.sztomek.pizzapp.data.converter.toPlace
import hu.sztomek.pizzapp.data.converter.toDomain
import hu.sztomek.pizzapp.data.converter.toPlaceDetails
import hu.sztomek.pizzapp.data.model.PlaceDbModel
import hu.sztomek.pizzapp.domain.Repository
import hu.sztomek.pizzapp.domain.error.DomainException
import hu.sztomek.pizzapp.domain.model.Friend
import hu.sztomek.pizzapp.domain.model.Place
import hu.sztomek.pizzapp.domain.model.PlaceDetails
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import java.io.IOException

class RepositoryImpl(private val databaseApi: DatabaseApi, private val webApi: WebApi) : Repository {

    private companion object {
        private const val WEB_API_FETCH_PERIOD = 60 * 60 * 1000
    }

    override fun listPlaces(): Flowable<List<Place>> {
        return databaseApi.listPlaces()
            .take(1)
            .compose(addDbErrorHandler())
            .flatMap { places ->
                if (shouldFetchFromWeb(places)) webListPlaces() else Flowable.just(places).map { it.map { it.toPlace() } }
            }
    }

    private fun shouldFetchFromWeb(dbPlaces: List<PlaceDbModel>): Boolean {
        return dbPlaces.isEmpty() || dbPlaces.map { it.fetched }.min() ?: 0L <= System.currentTimeMillis() - WEB_API_FETCH_PERIOD
}

    private fun webListPlaces(): Flowable<List<Place>> {
        return webApi.listPlaces()
            .compose(addWebErrorHandler())
            .map { it.list?.places }
            .flatMap { placeWebModels ->
                val dbModels = placeWebModels.map { it.toDb() }
                val mappedPlaces: MutableMap<PlaceDbModel, List<String>> = hashMapOf()
                dbModels.forEach { dbModel ->
                    mappedPlaces[dbModel] = placeWebModels.firstOrNull { it.id == dbModel.id.toString() } ?.openingHours ?: emptyList()
                }

                databaseApi.storePlaces(mappedPlaces)
                    .andThen(Single.just(placeWebModels))
            }
            .map { it.map { it.toPlace() } }
            .toFlowable()
    }

    override fun listFriends(): Flowable<List<Friend>> {
        return webApi.listFriends()
            .compose(addWebErrorHandler())
            .map { it.map { it.toDomain() } }
            .toFlowable()
    }

    override fun getPlace(id: String): Flowable<PlaceDetails> {
        return webApi.placeDetails(id)
            .compose(addWebErrorHandler())
            .flatMap { placeWebModel ->
                webApi.listFriends()
                    .compose(addWebErrorHandler())
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

    private fun <I> addWebErrorHandler(): SingleTransformer<I, I> {
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

    private fun <I> addDbErrorHandler(): FlowableTransformer<I, I> {
        return FlowableTransformer { upstream ->
            upstream.onErrorResumeNext { t: Throwable ->
                val domainException = when (t) {
                    is IOException -> DomainException.DatabaseError(t.message)
                    else -> DomainException.UnknownError(t.message)
                }
                Flowable.error(domainException)
            }
        }
    }
}