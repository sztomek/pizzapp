package hu.sztomek.pizzapp.presentation.screen.details

import android.content.Context
import android.content.Intent
import android.widget.RatingBar
import androidx.recyclerview.widget.LinearLayoutManager
import hu.sztomek.pizzapp.R
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.common.Action
import hu.sztomek.pizzapp.presentation.common.BaseActivity
import hu.sztomek.pizzapp.presentation.common.BaseViewModel
import hu.sztomek.pizzapp.presentation.common.UiError
import hu.sztomek.pizzapp.presentation.common.UiState
import hu.sztomek.pizzapp.presentation.common.dialog.BaseDialogFragment
import hu.sztomek.pizzapp.presentation.common.dialog.ErrorDialogFragment
import hu.sztomek.pizzapp.presentation.common.dialog.LoadingDialogFragment
import hu.sztomek.pizzapp.presentation.image.GlideApp
import hu.sztomek.pizzapp.presentation.model.DetailsUiModel
import kotlinx.android.synthetic.main.activity_details.*
import timber.log.Timber
import javax.inject.Inject

class DetailsActivity : BaseActivity<DetailsUiModel>() {

    companion object {
        private const val KEY_PLACE_ID = "placeId"
        private const val DIALOG_TAG = "shown_dialog"


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
    @Inject
    lateinit var friendsAdapter: FriendsAdapter

    override fun getViewModelClass(): Class<out BaseViewModel> {
        return DetailsViewModel::class.java
    }

    override fun initUi() {
        setContentView(R.layout.activity_details)

        viewModel.sendAction(Action.GetPlaceDetails(getPlaceId(intent)))

        details_bookmark.setOnClickListener {
            viewModel.sendAction(Action.BookmarkPlace(getPlaceId(intent)))
        }

        details_book_now.setOnClickListener {
            viewModel.sendAction(Action.BookPlace(getPlaceId(intent)))
        }

        details_rating.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    if (fromUser) {
                        viewModel.sendAction(Action.RatePlace(getPlaceId(intent), rating.toInt()))
                    }
                }

        details_friends.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        details_friends.adapter = friendsAdapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

        ErrorDialogFragment.create(
            ErrorDialogFragment.ErrorDialogModel(
                uiState.uiError.title,
                uiState.uiError.message,
                buttons
            ),
            object : ErrorDialogFragment.ErrorDialogClickListener {
                override fun onButtonClicked(which: ErrorDialogFragment.ErrorDialogButtons) {
                    when (which) {
                        ErrorDialogFragment.ErrorDialogButtons.POSITIVE -> {
                            if (uiState.uiError is UiError.Network) {
                                viewModel.sendAction(Action.GetPlaceDetails(getPlaceId(intent)))
                            }
                        }
                        else -> Timber.d("Unhandled button pressed")
                    }
                }
            }).show(supportFragmentManager, DIALOG_TAG)
    }

    private fun handleIdleState(uiState: UiState.IdleState) {
        hideDialog()

        val uiModel: DetailsUiModel = uiState.getDataAs()
        uiModel.details?.let {
            supportActionBar?.title = it.name
            GlideApp.with(details_image)
                .load(it.imageUrl)
                .error(R.drawable.ic_broken_image)
                .into(details_image)
            details_text.text = "Lorem ipsum dolor sit amet"
            details_rating.rating = it.rating
            details_rating_text.text = resources.getQuantityString(R.plurals.ratings, it.ratingCount)
            details_rating.setIsIndicator(it.userRating != null)
            details_bookmark.setImageDrawable(resources.getDrawable(if (it.bookmarked) R.drawable.ic_bookmarked else R.drawable.ic_bookmark))
            friendsAdapter.setData(it.friends)
        }
    }

}