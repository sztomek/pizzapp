package hu.sztomek.pizzapp.presentation.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import hu.sztomek.pizzapp.presentation.app.Pizzapplication

@Module
class AppModule {

    @Provides
    fun provideApplication(application: Pizzapplication): Application {
        return application
    }

}