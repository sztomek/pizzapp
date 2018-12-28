package hu.sztomek.pizzapp.presentation.model

import android.os.Parcel
import hu.sztomek.pizzapp.domain.model.Place
import hu.sztomek.pizzapp.presentation.common.PersistableModel
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MapUiModel(val places: List<Place> = emptyList()) : PersistableModel {

    private companion object : Parceler<MapUiModel> {
        override fun create(parcel: Parcel): MapUiModel {
            return MapUiModel()
        }

        override fun MapUiModel.write(parcel: Parcel, flags: Int) {
            // no-op
        }

    }
}
