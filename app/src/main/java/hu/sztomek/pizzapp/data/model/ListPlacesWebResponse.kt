package hu.sztomek.pizzapp.data.model

class ListPlacesWebResponse {

    var meta: Meta? = null
    var list: Places? = null

    class Meta {
        var total: Int = 0
        var precision: Int = 0
    }

    class Places {
        var places: List<PlaceWebModel> = emptyList()
    }

}