package hu.sztomek.pizzapp.device.impl

import android.app.Application
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import hu.sztomek.pizzapp.domain.Resources


class ResourcesImpl(private val application: Application) : Resources {

    private val resources: android.content.res.Resources = application.resources

    override fun getString(@StringRes resourceId: Int): String  = resources.getString(resourceId) ?: "String not found!"

    override fun getFormattedString(@StringRes resourceId: Int, formatArgs: Array<*>): String = resources.getString(resourceId, *formatArgs) ?: "String not found!"

    override fun getMarkerPadding(): Float {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels

        return Math.max(width, height) * 0.15f
    }

    override fun getDimension(@DimenRes resourceId: Int): Float {
        return resources.getDimension(resourceId)
    }

    @ColorInt
    override fun getColor(@ColorRes resourceId: Int): Int {
        return ContextCompat.getColor(application, resourceId)
    }
}
