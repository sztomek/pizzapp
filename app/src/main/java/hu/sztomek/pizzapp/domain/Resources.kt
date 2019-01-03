package hu.sztomek.pizzapp.domain

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes

interface Resources {

    fun getString(@StringRes resourceId: Int): String
    fun getFormattedString(@StringRes resourceId: Int, formatArgs: Array<*>): String
    fun getMarkerPadding(): Float
    fun getDimension(@DimenRes resourceId: Int): Float
    @ColorInt fun getColor(@ColorRes resourceId: Int): Int

}