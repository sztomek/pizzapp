package hu.sztomek.pizzapp.presentation.screen.details

import android.content.Context
import android.content.Intent
import android.widget.Toast
import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.presentation.common.BaseActivity
import hu.sztomek.pizzapp.presentation.common.BaseViewModel
import hu.sztomek.pizzapp.presentation.common.UiState
import hu.sztomek.pizzapp.presentation.model.DetailsUiModel
import javax.inject.Inject

class DetailsActivity : BaseActivity<DetailsUiModel>() {

    companion object {
        private const val KEY_PLACE_ID = "placeId"

        fun starter(context: Context, id: String): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(KEY_PLACE_ID, id)
            }
        }

        private fun getPlaceId(intent: Intent?): String {
            return intent?.getStringExtra(KEY_PLACE_ID) ?: throw IllegalArgumentException("Id must not be null")
        }
    }

    @Inject
    lateinit var resources: Resources

    override fun getViewModelClass(): Class<out BaseViewModel> {
        return DetailsViewModel::class.java
    }

    override fun initUi() {
        setContentView(R.layout.activity_details)

        viewModel.sendAction(Action.GetPlaceDetails(getPlaceId(intent)))
    }

    override fun getDefaultInitialState(): DetailsUiModel {
        return DetailsUiModel(getPlaceId(intent))
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
        // TODO progress dialog
    }

    private fun handleErrorState(uiState: UiState.ErrorState) {
        // TODO handle error properly - show alert dialog
        showError(uiState.uiError.message)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun handleIdleState(uiState: UiState.IdleState) {
        val uiModel: DetailsUiModel = uiState.getDataAs()
        // TODO handle details idle state
    }

}