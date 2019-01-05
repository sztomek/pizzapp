package hu.sztomek.pizzapp.domain

import android.graphics.drawable.Drawable
import androidx.annotation.*

interface Resources {

    fun getString(@StringRes resourceId: Int): String
    fun getFormattedString(@StringRes resourceId: Int, formatArgs: Array<*>): String
    fun getQuantityString(@PluralsRes resourceId: Int, quantity: Int): String
    fun getMarkerPadding(): Float
    fun getDimension(@DimenRes resourceId: Int): Float
    @ColorInt fun getColor(@ColorRes resourceId: Int): Int
    fun getDrawable(@DrawableRes resourceId: Int): Drawable

}