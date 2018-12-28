package hu.sztomek.pizzapp.device.impl

import hu.sztomek.pizzapp.domain.Resources


class ResourcesImpl(private val resources: android.content.res.Resources) : Resources {

    override fun getString(resourceId: Int): String  = resources.getString(resourceId) ?: "String not found!"

    override fun getFormattedString(resourceId: Int, formatArgs: Array<*>): String = resources.getString(resourceId, *formatArgs) ?: "String not found!"
}