package hu.sztomek.pizzapp.data.model

class ListPlacesWebResponse {

    var meta: Meta? = null
    var list: List<PlaceWebModel> = emptyList()

    class Meta {
        var total: Int = 0
        var precision: Int = 0
    }

}