package hu.sztomek.pizzapp.presentation.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import hu.sztomek.pizzapp.data.di.DataModule
import hu.sztomek.pizzapp.device.di.DeviceModule
import hu.sztomek.pizzapp.presentation.app.Pizzapplication
import hu.sztomek.pizzapp.presentation.di.module.ActivityBinderModule
import hu.sztomek.pizzapp.presentation.di.module.AppModule
import hu.sztomek.pizzapp.presentation.di.module.NavigatorModule
import hu.sztomek.pizzapp.presentation.di.module.ViewModelBinderModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ViewModelBinderModule::class,
        ActivityBinderModule::class,
        DataModule::class,
        DeviceModule::class,
        NavigatorModule::class
    )
)
interface AppComponent : AndroidInjector<Pizzapplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Pizzapplication): Builder

        fun build(): AppComponent
    }
}