package hu.sztomek.pizzapp.domain.interactor

import hu.sztomek.pizzapp.domain.Repository
import hu.sztomek.pizzapp.domain.model.Friend
import io.reactivex.Flowable
import javax.inject.Inject

class FriendsInteractor @Inject constructor(private val repository: Repository) {

    fun listFriends(): Flowable<List<Friend>> {
        return repository.listFriends()
    }

    // TODO do we need this?
    fun getFriend(): Flowable<Friend> {
        return repository.listFriends()
            .flatMapIterable { it }
            .take(1)
    }
}