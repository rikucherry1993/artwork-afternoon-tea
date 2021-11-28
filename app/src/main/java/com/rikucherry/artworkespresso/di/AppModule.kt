package com.rikucherry.artworkespresso.di

import android.app.Application
import android.content.Context
import com.rikucherry.artworkespresso.ISecrets
import com.rikucherry.artworkespresso.Secrets
import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.tool.SharedPreferenceHelper
import com.rikucherry.artworkespresso.feature_authentication.data.remote.AuthenticationApiService
import com.rikucherry.artworkespresso.feature_authentication.data.repository.AuthenticationRepository
import com.rikucherry.artworkespresso.feature_authentication.domain.repository.AuthenticationRepositoryImpl
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application) : Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPreferenceHelper(context: Context): SharedPreferenceHelper {
        return SharedPreferenceHelper(context)
    }

    @Provides
    @Singleton
    fun provideAuthenticationApi() : AuthenticationApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()
            .create(AuthenticationApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthenticationRepository(authApi: AuthenticationApiService)
            : AuthenticationRepository {
        return AuthenticationRepositoryImpl(authApi)
    }

    @Provides
    @Singleton
    fun providesSecrets() : ISecrets {
        return Secrets()
    }
}