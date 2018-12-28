package hu.sztomek.pizzapp.data.model

class PlaceWebModel {

    var id: String? = null
    var name: String? = null
    var phone: String? = null
    var website: String? = null
    var formattedAddress: String? = null
    var city: String? = null
    var openingHours: List<String> = emptyList()
    var longitude: Double? = null
    var latitude: Double? = null
    var images: List<Image> = emptyList()
    var friendIds: List<String> = emptyList()

    class Image {

        var id: String? = null
        var url: String? = null
        var caption: String? = null
        var expiration: String? = null

    }

}