package hu.sztomek.pizzapp.presentation.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.sztomek.pizzapp.presentation.di.PizzaVMFactory
import hu.sztomek.pizzapp.presentation.di.ViewModelKey
import hu.sztomek.pizzapp.presentation.screen.details.DetailsViewModel
import hu.sztomek.pizzapp.presentation.screen.map.MapViewModel

@Module
interface ViewModelBinderModule {

    @Binds
    fun bindViewModelFactory(factory: PizzaVMFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    fun bindMapViewModel(viewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    fun bindDetailsViewModel(viewModel: DetailsViewModel): ViewModel

}