package hu.sztomek.pizzapp.data.di

import android.app.Application
import dagger.Module
import dagger.Provides
import hu.sztomek.pizzapp.data.RepositoryImpl
import hu.sztomek.pizzapp.data.api.DatabaseApi
import hu.sztomek.pizzapp.data.api.DatabaseApiImpl
import hu.sztomek.pizzapp.data.api.WebApi
import hu.sztomek.pizzapp.domain.Repository
import io.objectbox.BoxStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl() = "https://pizzaplaces.free.beeceptor.com/"

    @Singleton
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Timber.d(message) }
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@Named("baseUrl") baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideWebApi(retrofit: Retrofit): WebApi {
        return retrofit.create(WebApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBoxStore(application: Application): BoxStore {
        return MyObjectBox.builder().androidContext(application).build()
    }

    @Singleton
    @Provides
    fun provideDatabaseApi(boxStore: BoxStore): DatabaseApi {
        return DatabaseApiImpl(boxStore)
    }

    @Singleton
    @Provides
    fun provideRepository(databaseApi: DatabaseApi, webApi: WebApi): Repository {
        return RepositoryImpl(databaseApi, webApi)
    }
}