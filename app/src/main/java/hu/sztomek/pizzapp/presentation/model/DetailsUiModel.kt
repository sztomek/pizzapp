package hu.sztomek.pizzapp.presentation.model

import android.os.Parcel
import hu.sztomek.pizzapp.domain.model.Friend
import hu.sztomek.pizzapp.domain.model.Place
import hu.sztomek.pizzapp.presentation.common.PersistableModel
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import java.lang.IllegalArgumentException

@Parcelize
data class DetailsUiModel(val itemId: String,
                          val details: Place? = null,
                          val friends: List<Friend>? = emptyList()) : PersistableModel {

    private companion object : Parceler<DetailsUiModel> {
        override fun create(parcel: Parcel): DetailsUiModel {
            return DetailsUiModel(parcel.readString() ?: throw IllegalArgumentException("Failed to read id from bundle"))
        }

        override fun DetailsUiModel.write(parcel: Parcel, flags: Int) {
            parcel.writeString(itemId)
        }

    }

}