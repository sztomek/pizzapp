package hu.sztomek.pizzapp.presentation.navigator

import androidx.appcompat.app.AppCompatActivity

interface Navigator {

    fun takeActivity(activity: AppCompatActivity)

    fun goBack()
    fun showDetails(id: String)

}