package hu.sztomek.pizzapp.presentation.common.dialog

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialogFragment : DialogFragment() {

    companion object {
        const val ARG_MODEL = "dialog_model"

        fun createArgs(model: BaseDialogModel): Bundle {
            return Bundle().apply {
                putParcelable(ARG_MODEL, model)
            }
        }

        inline fun <reified T : BaseDialogModel> getModelFromArgs(arguments: Bundle?): T {
            return arguments?.getParcelable(ARG_MODEL)!!
        }
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        manager?.let {
            val transaction = it.beginTransaction()
            val previous = it.findFragmentByTag(tag)
            if (previous != null) {
                transaction.remove(previous)
            }

            super.show(transaction, tag)
            manager.executePendingTransactions()
        }
    }

    interface BaseDialogModel : Parcelable {
        val message: String
    }

}