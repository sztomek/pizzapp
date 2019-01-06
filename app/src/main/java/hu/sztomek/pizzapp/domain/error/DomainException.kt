package hu.sztomek.pizzapp.domain.error

sealed class DomainException(message: String?) : Throwable(message) {

    data class UnknownError(override val message: String? = null) : DomainException(message)
    data class NetworkError(override val message: String? = null) : DomainException(message)
    data class DatabaseError(override val message: String? = null) : DomainException(message)

}