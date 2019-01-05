package hu.sztomek.pizzapp.presentation.screen.details

import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.WorkSchedulers
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.domain.common.Operation
import hu.sztomek.pizzapp.domain.error.DomainException
import hu.sztomek.pizzapp.domain.interactor.PlacesInteractor
import hu.sztomek.pizzapp.presentation.common.BaseViewModel
import hu.sztomek.pizzapp.presentation.common.UiError
import hu.sztomek.pizzapp.presentation.common.UiState
import hu.sztomek.pizzapp.presentation.model.DetailsUiModel
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailsViewModel @Inject constructor(workSchedulers: WorkSchedulers,
                                           private val placesInteractor: PlacesInteractor,
                                           private val resources: Resources
) : BaseViewModel(workSchedulers) {

    override fun invokeActions(): FlowableTransformer<Action, Operation> {
        return FlowableTransformer { upstream ->
            Flowable.merge(
                upstream.ofType(Action.GetPlaceDetails::class.java)
                    .flatMap { action ->
                        placesInteractor.getPlace(action)
                            .map<Operation> { Operation.Completed(action, it) }
                            .startWith(Operation.InProgress(action))
                            .onErrorReturn { Operation.Failed(action, wrapExceptionWhenNecessary(it)) }
                    },
                upstream.ofType(Action.RatePlace::class.java)
                    .flatMap { action -> Flowable.timer(1, TimeUnit.SECONDS)
                        .map<Operation> { Operation.Completed(action, Any()) }
                        .startWith(Operation.InProgress(action))
                    },
                upstream.ofType(Action.BookPlace::class.java)
                    .flatMap {action ->
                        Flowable.timer(1, TimeUnit.SECONDS)
                            .map<Operation> { Operation.Completed(action, Any()) }
                            .startWith(Operation.InProgress(action))
                    },
                upstream.ofType(Action.BookmarkPlace::class.java)
                    .flatMap { action ->
                        Flowable.timer(1, TimeUnit.SECONDS)
                            .map<Operation> { Operation.Completed(action, Any()) }
                            .startWith(Operation.InProgress(action))
                    }
            )
        }
    }

    override fun getReducerFunction(): BiFunction<UiState, Operation, UiState> {
        return BiFunction { oldState, operation ->
            when (operation) {
                is Operation.InProgress -> UiState.LoadingState(resources.getString(R.string.msg_loading), oldState.data)
                is Operation.Failed -> UiState.ErrorState(mapUiError(operation.exception), oldState.data)
                is Operation.Completed ->produceNewIdleState(oldState, operation)

            }
        }
    }

    private fun produceNewIdleState(oldState: UiState, operation: Operation.Completed): UiState.IdleState {
        val oldDetailsUiModel = oldState.getDataAs<DetailsUiModel>()
        val newState = when (operation.action) {
            is Action.GetPlaceDetails -> oldDetailsUiModel.copy(details = operation.resultAs())
            is Action.RatePlace -> oldDetailsUiModel.copy(
                details = oldDetailsUiModel.details?.copy(
                    rating = (oldDetailsUiModel.details.rating * oldDetailsUiModel.details.ratingCount + operation.action.rating) / (oldDetailsUiModel.details.ratingCount + 1),
                    ratingCount = oldDetailsUiModel.details.ratingCount + 1,
                    userRating = operation.action.rating
                )
            )
            is Action.BookmarkPlace -> oldDetailsUiModel.copy(details = oldDetailsUiModel.details?.copy(bookmarked = oldDetailsUiModel.details.bookmarked.not()))
            is Action.BookPlace -> oldDetailsUiModel
            else -> {
                Timber.d("Unhandled action: [${operation.action}]")
                oldDetailsUiModel
            }
        }

        return UiState.IdleState(newState)
    }

    private fun mapUiError(error: DomainException): UiError {
        return when (error) {
            is DomainException.UnknownError -> UiError.General(resources.getString(R.string.title_unknown_error), resources.getString(R.string.msg_unknown_error))
            is DomainException.NetworkError -> UiError.Network(resources.getString(R.string.title_network_error), resources.getFormattedString(R.string.msg_format_network, arrayOf(error.message)))
        }
    }

}