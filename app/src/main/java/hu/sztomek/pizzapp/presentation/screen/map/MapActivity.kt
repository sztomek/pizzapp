package hu.sztomek.pizzapp.presentation.screen.map

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.domain.model.Place
import hu.sztomek.pizzapp.presentation.common.BaseActivity
import hu.sztomek.pizzapp.presentation.common.BaseViewModel
import hu.sztomek.pizzapp.presentation.common.UiState
import hu.sztomek.pizzapp.presentation.converter.toMarker
import hu.sztomek.pizzapp.presentation.model.MapUiModel
import kotlinx.android.synthetic.main.activity_map.*
import javax.inject.Inject

class MapActivity : BaseActivity<MapUiModel>() {

    private var map: GoogleMap? = null

    @Inject
    lateinit var resources: Resources

    @Inject
    lateinit var infoWindowAdapter: InfoWindowAdapter

    override fun getViewModelClass(): Class<out BaseViewModel> {
        return MapViewModel::class.java
    }

    override fun initUi() {
        setContentView(R.layout.activity_map)

        map_mapview.getMapAsync {
            map = it
            initMap()
            displayPlacesWhenMapReady()
        }
    }

    private fun initMap() {
        map?.let {
            it.setInfoWindowAdapter(infoWindowAdapter)

            it.setOnInfoWindowClickListener {
                navigator.showDetails((it.tag as Place).id)
            }
        }
    }

    private fun displayPlacesWhenMapReady() {
        val boundsBuilder = LatLngBounds.builder()

        with(viewModel.stateStream.value?.getDataAs<MapUiModel>()?.places) {
            this?.forEach {
                val markerOptions = it.toMarker(resources)
                val marker = map?.addMarker(markerOptions)
                marker?.tag = it
                boundsBuilder.include(markerOptions.position)
            }

            if (this != null && !this.isEmpty()) {
                map?.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), resources.getMarkerPadding().toInt()))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        map_mapview.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        map_mapview.onStart()
    }

    override fun onResume() {
        super.onResume()

        map_mapview.onResume()

        viewModel.sendAction(Action.ListPlaces)
    }

    override fun onPause() {
        map_mapview.onPause()

        super.onPause()
    }

    override fun onStop() {
        map_mapview.onStop()

        super.onStop()
    }

    override fun onDestroy() {
        map_mapview.onDestroy()

        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        map_mapview.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        map_mapview.onLowMemory()

        super.onLowMemory()
    }

    override fun getDefaultInitialState(): MapUiModel {
        return MapUiModel()
    }

    override fun render(uiState: UiState?) {
        uiState?.let {
            when (it) {
                is UiState.LoadingState -> handleLoadingState(it)
                is UiState.ErrorState -> handleErrorState(it)
                is UiState.IdleState -> handleIdleState(it)
            }
        }
    }

    private fun handleLoadingState(uiState: UiState.LoadingState) {
        // TODO handle loading state
    }

    private fun handleErrorState(uiState: UiState.ErrorState) {
        // TODO handle error properly - show alert dialog
        Toast.makeText(this, uiState.uiError.message, Toast.LENGTH_LONG).show()
    }

    private fun handleIdleState(uiState: UiState.IdleState) {
        displayPlacesWhenMapReady()
    }

}
