package hu.sztomek.pizzapp.presentation.screen.map

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.domain.model.Place
import hu.sztomek.pizzapp.presentation.common.BaseActivity
import hu.sztomek.pizzapp.presentation.common.BaseViewModel
import hu.sztomek.pizzapp.presentation.common.UiError
import hu.sztomek.pizzapp.presentation.common.UiState
import hu.sztomek.pizzapp.presentation.common.dialog.BaseDialogFragment
import hu.sztomek.pizzapp.presentation.common.dialog.ErrorDialogFragment
import hu.sztomek.pizzapp.presentation.common.dialog.LoadingDialogFragment
import hu.sztomek.pizzapp.presentation.converter.toMarker
import hu.sztomek.pizzapp.presentation.model.MapUiModel
import kotlinx.android.synthetic.main.activity_map.*
import timber.log.Timber
import javax.inject.Inject

class MapActivity : BaseActivity<MapUiModel>() {

    companion object {
        private const val DIALOG_TAG = "shown_dialog"
    }

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
                map?.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        boundsBuilder.build(),
                        resources.getMarkerPadding().toInt()
                    )
                )
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

    private inline fun <reified T> findFragmentByTag(tag: String): T? {
        return supportFragmentManager.findFragmentByTag(tag) as? T
    }

    private fun hideDialog() {
        findFragmentByTag<BaseDialogFragment>(DIALOG_TAG)?.apply {
            dismiss()
        }
    }

    private fun handleLoadingState(uiState: UiState.LoadingState) {
        LoadingDialogFragment.create(LoadingDialogFragment.LoadingDialogModel(uiState.message))
            .show(supportFragmentManager, DIALOG_TAG)
    }

    private fun handleErrorState(uiState: UiState.ErrorState) {
        hideDialog()

        val buttons: Map<ErrorDialogFragment.ErrorDialogButtons, String> = when (uiState.uiError) {
            is UiError.General -> mapOf(ErrorDialogFragment.ErrorDialogButtons.NEUTRAL to resources.getString(android.R.string.ok))
            is UiError.Network -> mapOf(
                ErrorDialogFragment.ErrorDialogButtons.POSITIVE to resources.getString(R.string.btn_retry),
                ErrorDialogFragment.ErrorDialogButtons.NEGATIVE to resources.getString(R.string.btn_cancel)
            )
        }

        ErrorDialogFragment.create(ErrorDialogFragment.ErrorDialogModel(
            uiState.uiError.title,
            uiState.uiError.message,
            buttons
        ),
            object : ErrorDialogFragment.ErrorDialogClickListener {
                override fun onButtonClicked(which: ErrorDialogFragment.ErrorDialogButtons) {
                    when (which) {
                        ErrorDialogFragment.ErrorDialogButtons.POSITIVE -> {
                            if (uiState.uiError is UiError.Network) {
                                viewModel.sendAction(Action.ListPlaces)
                            }
                        }
                        else -> Timber.d("Unhandled button pressed")
                    }
                }
            }).show(supportFragmentManager, DIALOG_TAG)
    }

    private fun handleIdleState(uiState: UiState.IdleState) {
        hideDialog()
        displayPlacesWhenMapReady()
    }

}
