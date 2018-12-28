package hu.sztomek.pizzapp.presentation.screen.details

import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.WorkSchedulers
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.domain.common.Operation
import hu.sztomek.pizzapp.presentation.common.BaseViewModel
import hu.sztomek.pizzapp.presentation.common.UiState
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class DetailsViewModel @Inject constructor(workSchedulers: WorkSchedulers,
                                           private val resources: Resources
) : BaseViewModel(workSchedulers) {

    override fun invokeActions(): FlowableTransformer<Action, Operation> {
        return FlowableTransformer {
            // TODO implement me
            it.flatMap<Operation> { Flowable.just(Operation.InProgress(it)) }
        }
    }

    override fun getReducerFunction(): BiFunction<UiState, Operation, UiState> {
        return BiFunction { oldState, operation ->
            // TODO implement me
            oldState
        }
    }

}