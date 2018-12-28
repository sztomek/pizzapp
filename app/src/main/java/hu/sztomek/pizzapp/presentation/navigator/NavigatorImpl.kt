package hu.sztomek.pizzapp.presentation.navigator

import androidx.appcompat.app.AppCompatActivity

class NavigatorImpl(private var activity: AppCompatActivity? = null) : Navigator {

    override fun takeActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    override fun goBack() {
        activity?.onBackPressed()
    }

    override fun showDetails(id: String) {
        activity?.let {
            // TODO start details
        }
    }
}