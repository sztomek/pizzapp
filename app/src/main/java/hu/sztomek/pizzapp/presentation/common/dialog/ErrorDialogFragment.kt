package hu.sztomek.pizzapp.presentation.common.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import kotlinx.android.parcel.Parcelize

class ErrorDialogFragment : BaseDialogFragment() {

    interface ErrorDialogClickListener {
        fun onButtonClicked(which: ErrorDialogFragment.ErrorDialogButtons)
    }

    enum class ErrorDialogButtons {
        NEUTRAL,
        POSITIVE,
        NEGATIVE
    }

    @Parcelize
    data class ErrorDialogModel(val title: String,
                                override val message: String,
                                val buttonLabels: Map<ErrorDialogButtons, String> = emptyMap()
    ) : BaseDialogModel


    companion object {

        fun create(model: ErrorDialogModel, clickListener: ErrorDialogClickListener): ErrorDialogFragment {
            val errorDialogFragment = ErrorDialogFragment()
            return errorDialogFragment.apply {
                this.clickListener = clickListener
                arguments = BaseDialogFragment.createArgs(model)
            }
        }

    }

    private val model: ErrorDialogModel by lazy {
       BaseDialogFragment.getModelFromArgs<ErrorDialogModel>(arguments)
    }

    private lateinit var clickListener: ErrorDialogClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        val builder = AlertDialog.Builder(activity)
            .setTitle(model.title)
            .setMessage(model.message)
        model.buttonLabels.forEach {
            when (it.key) {
                ErrorDialogButtons.NEUTRAL -> builder.setNeutralButton(it.value) { _, _ -> clickListener.onButtonClicked(ErrorDialogButtons.NEUTRAL) }
                ErrorDialogButtons.NEGATIVE -> builder.setNegativeButton(it.value) { _, _ -> clickListener.onButtonClicked(ErrorDialogButtons.NEGATIVE)}
                ErrorDialogButtons.POSITIVE -> builder.setPositiveButton(it.value) { _, _ -> clickListener.onButtonClicked(ErrorDialogButtons.POSITIVE)}
            }
        }

        return builder.create()
    }
}