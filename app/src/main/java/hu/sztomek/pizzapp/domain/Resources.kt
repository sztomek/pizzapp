package hu.sztomek.pizzapp.domain

import androidx.annotation.StringRes

interface Resources {

    fun getString(@StringRes resourceId: Int): String
    fun getFormattedString(@StringRes resourceId: Int, formatArgs: Array<*>): String

}