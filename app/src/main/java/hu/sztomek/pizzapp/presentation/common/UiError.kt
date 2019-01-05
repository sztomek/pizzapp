package hu.sztomek.pizzapp.presentation.common

sealed class UiError {

    abstract val title: String
    abstract val message: String

        data class General(override val title: String, override val message: String) : UiError()
        data class Network(override val title: String, override val message: String) : UiError()
}