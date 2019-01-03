package hu.sztomek.pizzapp.presentation.converter

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.model.Location
import hu.sztomek.pizzapp.domain.model.Place

private fun Location.toLatLng() = LatLng(latitude, longitude)

fun Place.toMarker(resources: Resources) = MarkerOptions()
    .position(location.toLatLng())
    .title(name)
    .snippet(resources.getString(if (open) R.string.map_place_open else R.string.map_place_closed))