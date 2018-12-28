package hu.sztomek.pizzapp.domain.common

import hu.sztomek.pizzapp.domain.error.DomainException

sealed class Operation {

    abstract val action: Action

    data class InProgress(override val action: Action) : Operation()

    data class Completed(override val action: Action, val result: Any) : Operation() {

        inline fun <reified T> resultAs(): T? {
            return result as? T
        }

    }

    data class Failed(override val action: Action, val exception: DomainException) : Operation()

}