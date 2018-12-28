package hu.sztomek.pizzapp.device.di

import android.app.Application
import dagger.Module
import dagger.Provides
import hu.sztomek.pizzapp.device.impl.ResourcesImpl
import hu.sztomek.pizzapp.device.impl.WorkSchedulersImpl
import hu.sztomek.pizzapp.domain.Resources
import hu.sztomek.pizzapp.domain.WorkSchedulers
import javax.inject.Singleton

@Module
class DeviceModule {

    @Singleton
    @Provides
    fun provideResources(application: Application): Resources {
        return ResourcesImpl(application.resources)
    }

    @Singleton
    @Provides
    fun provideWorkSchedulers(): WorkSchedulers {
        return WorkSchedulersImpl()
    }

}