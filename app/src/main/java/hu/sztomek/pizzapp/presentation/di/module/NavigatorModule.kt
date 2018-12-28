package hu.sztomek.pizzapp.presentation.di.module

import dagger.Module
import dagger.Provides
import hu.sztomek.pizzapp.presentation.navigator.Navigator
import hu.sztomek.pizzapp.presentation.navigator.NavigatorImpl

@Module
class NavigatorModule {

    @Provides
    fun provideNavigator(): Navigator {
        return NavigatorImpl()
    }

}