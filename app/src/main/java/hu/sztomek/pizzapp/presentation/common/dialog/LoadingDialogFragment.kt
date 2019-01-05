package hu.sztomek.pizzapp.presentation.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import hu.sztomek.pizzapp.R
import kotlinx.android.parcel.Parcelize

class LoadingDialogFragment : BaseDialogFragment() {

    @Parcelize
    data class LoadingDialogModel(override val message: String) :
        BaseDialogModel

    companion object {
        fun create(model: LoadingDialogModel): LoadingDialogFragment {
            val loadingDialogFragment = LoadingDialogFragment()
            createArgs(model)
                .apply { loadingDialogFragment.arguments = this }

            return loadingDialogFragment
        }
    }

    private val message: String by lazy {
        getModelFromArgs<LoadingDialogModel>(
            arguments
        ).message
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.LoadingDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false

        return inflater.inflate(R.layout.layout_loading_indicator, container)
            .apply { findViewById<TextView>(R.id.loading_indicator_message).text = message }
    }
}