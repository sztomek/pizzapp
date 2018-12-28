package hu.sztomek.pizzapp.presentation.common

sealed class UiError {

    abstract val message: String

        data class General(override val message: String) : UiError()

}