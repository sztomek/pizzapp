package hu.sztomek.pizzapp.presentation.model

import android.os.Parcel
import hu.sztomek.pizzapp.domain.model.PlaceDetails
import hu.sztomek.pizzapp.presentation.common.PersistableModel
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailsUiModel(val itemId: String,
                          val details: PlaceDetails? = null) : PersistableModel {

    private companion object : Parceler<DetailsUiModel> {
        override fun create(parcel: Parcel): DetailsUiModel {
            return DetailsUiModel(parcel.readString() ?: throw IllegalArgumentException("Failed to read id from bundle"))
        }

        override fun DetailsUiModel.write(parcel: Parcel, flags: Int) {
            parcel.writeString(itemId)
        }

    }

}