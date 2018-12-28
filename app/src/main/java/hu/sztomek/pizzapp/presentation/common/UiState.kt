package hu.sztomek.pizzapp.presentation.common

sealed class UiState {

    abstract val data: PersistableModel

    inline fun <reified T> getDataAs(): T {
        return data as T
    }

    data class IdleState(override val data: PersistableModel): UiState()
    data class LoadingState(val message: String, override val data: PersistableModel): UiState()
    data class ErrorState(val uiError: UiError, override val data: PersistableModel): UiState()

}