package hu.sztomek.pizzapp.presentation.screen.map

import android.widget.Toast
import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.presentation.common.BaseActivity
import hu.sztomek.pizzapp.presentation.common.BaseViewModel
import hu.sztomek.pizzapp.presentation.common.UiState
import hu.sztomek.pizzapp.presentation.model.MapUiModel

class MapActivity : BaseActivity<MapUiModel>() {

    override fun getViewModelClass(): Class<out BaseViewModel> {
        return MapViewModel::class.java
    }

    override fun initUi() {
        setContentView(R.layout.activity_list)

        // TODO setup ui
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
        val uiModel: MapUiModel = uiState.getDataAs()
        // TODO handle map idle state
    }

}
