package hu.sztomek.pizzapp.presentation.screen.map

import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.WorkSchedulers
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.domain.common.Operation
import hu.sztomek.pizzapp.domain.interactor.PlacesInteractor
import hu.sztomek.pizzapp.domain.model.Place
import hu.sztomek.pizzapp.presentation.common.BaseViewModel
import hu.sztomek.pizzapp.presentation.common.UiError
import hu.sztomek.pizzapp.presentation.common.UiState
import hu.sztomek.pizzapp.presentation.model.MapUiModel
import io.reactivex.FlowableTransformer
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class MapViewModel @Inject constructor(workSchedulers: WorkSchedulers,
                                       private val placesInteractor: PlacesInteractor,
                                       private val resources: Resources
) : BaseViewModel(workSchedulers) {

    override fun invokeActions(): FlowableTransformer<Action, Operation> {
        return FlowableTransformer {
            it.ofType(Action.ListPlaces::class.java)
                .flatMap { action -> placesInteractor.listPlaces()
                    .map<Operation> { Operation.Completed(action, it) }
                    .startWith(Operation.InProgress(action))
                }
        }
    }

    override fun getReducerFunction(): BiFunction<UiState, Operation, UiState> {
        return BiFunction { oldState, operation ->
            when (operation) {
                is Operation.InProgress -> UiState.LoadingState(resources.getString(R.string.msg_loading), oldState.data)
                is Operation.Failed -> UiState.ErrorState(UiError.General(resources.getString(R.string.msg_unknown_error)), oldState.data)
                is Operation.Completed -> UiState.IdleState(MapUiModel(operation.resultAs<List<Place>>() ?: emptyList()))
            }
        }
    }

}