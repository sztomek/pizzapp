package hu.sztomek.pizzapp.presentation.navigator

import androidx.appcompat.app.AppCompatActivity
import hu.sztomek.pizzapp.presentation.screen.details.DetailsActivity

class NavigatorImpl(private var activity: AppCompatActivity? = null) : Navigator {

    override fun takeActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    override fun goBack() {
        activity?.onBackPressed()
    }

    override fun showDetails(id: String) {
        activity?.let {
            it.startActivity(DetailsActivity.starter(it, id))
        }
    }
}