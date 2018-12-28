package hu.sztomek.pizzapp.presentation.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import hu.sztomek.pizzapp.presentation.di.ActivityScope
import hu.sztomek.pizzapp.presentation.screen.details.DetailsActivity
import hu.sztomek.pizzapp.presentation.screen.map.MapActivity

@Module
interface ActivityBinderModule {

    @ContributesAndroidInjector
    @ActivityScope
    fun bindListActivity(): MapActivity

    @ContributesAndroidInjector
    @ActivityScope
    fun bindDetailsActivity(): DetailsActivity

}