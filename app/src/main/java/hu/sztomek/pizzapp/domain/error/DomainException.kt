package hu.sztomek.pizzapp.domain.error

sealed class DomainException(message: String?) : Throwable(message) {

    data class UnknownError(override val message: String? = null) : DomainException(message)

}