package hu.sztomek.pizzapp.device.impl

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import hu.sztomek.pizzapp.domain.Resources
import java.lang.IllegalArgumentException


class ResourcesImpl(private val application: Application) : Resources {

    private val resources: android.content.res.Resources = application.resources

    override fun getString(@StringRes resourceId: Int): String = resources.getString(resourceId) ?: "String not found!"

    override fun getFormattedString(@StringRes resourceId: Int, formatArgs: Array<*>): String =
        resources.getString(resourceId, *formatArgs) ?: "String not found!"

    override fun getQuantityString(@PluralsRes resourceId: Int, quantity: Int): String {
        return resources.getQuantityString(resourceId, quantity, quantity)
    }

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

    override fun getDrawable(@DrawableRes resourceId: Int): Drawable {
        return ContextCompat.getDrawable(application, resourceId)
            ?: throw IllegalArgumentException("Drawable with resource id [$resourceId] not found!")
    }
}
