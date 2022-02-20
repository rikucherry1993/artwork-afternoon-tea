package com.rikucherry.artworkespresso.di

import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.common.data.local.ArtworkEspressoDatabase
import com.rikucherry.artworkespresso.feature_daily_brief.data.remote.DailyBriefApiService
import com.rikucherry.artworkespresso.feature_daily_brief.data.repository.DailyBriefRepository
import com.rikucherry.artworkespresso.feature_daily_brief.domain.repository.DailyBriefRepositoryImpl
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
object DailyBriefModule {

    @Provides
    @Singleton
    fun provideDailyBriefApi() : DailyBriefApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL + Constants.BASE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()
            .create(DailyBriefApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDailyBriefRepository(
        database: ArtworkEspressoDatabase,
        dailyBriefApi: DailyBriefApiService
    ) : DailyBriefRepository {
        return DailyBriefRepositoryImpl(database.savedArtworks(),dailyBriefApi)
    }

}