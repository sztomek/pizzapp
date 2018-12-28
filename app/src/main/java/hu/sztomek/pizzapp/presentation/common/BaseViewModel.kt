package hu.sztomek.pizzapp.presentation.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.sztomek.pizzapp.domain.WorkSchedulers
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.domain.common.Operation
import hu.sztomek.pizzapp.domain.error.DomainException
import io.reactivex.FlowableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import timber.log.Timber

abstract class BaseViewModel(private val workSchedulers: WorkSchedulers) : ViewModel() {

    private val actionStream: FlowableProcessor<Action> = PublishProcessor.create<Action>()
    private val disposables = CompositeDisposable()
    private var subscribedToActions = false

    val stateStream: MutableLiveData<UiState> = MutableLiveData()

    override fun onCleared() {
        disposables.dispose()
        subscribedToActions = false
        super.onCleared()
    }

    fun takeInitialState(initialState: UiState) {
        if (subscribedToActions) {
            return
        }

        actionStream
            .observeOn(workSchedulers.worker())
            .compose(invokeActions())
            .scan(stateStream.value ?: initialState, getReducerFunction())
            .observeOn(workSchedulers.ui())
            .subscribe(
                { stateStream.value = it },
                Timber::e
            ).apply {
                disposables.add(this)
                subscribedToActions = true
            }
    }

    fun sendAction(action: Action) {
        actionStream.onNext(action)
    }

    protected fun wrapExceptionWhenNecessary(throwable: Throwable): DomainException {
        return if (throwable is DomainException) throwable
        else DomainException.UnknownError(throwable.message)
    }

    protected abstract fun invokeActions(): FlowableTransformer<Action, Operation>
    protected abstract fun getReducerFunction(): BiFunction<UiState, Operation, UiState>
}