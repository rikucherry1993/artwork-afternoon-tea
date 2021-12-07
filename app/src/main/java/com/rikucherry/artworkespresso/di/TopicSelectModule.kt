package com.rikucherry.artworkespresso.di

import com.rikucherry.artworkespresso.common.Constants
import com.rikucherry.artworkespresso.feature_topic_selection.data.remote.TopicSelectApiService
import com.rikucherry.artworkespresso.feature_topic_selection.data.repository.TopicSelectRepository
import com.rikucherry.artworkespresso.feature_topic_selection.domain.repository.TopicSelectRepositoryImpl
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
object TopicSelectModule {

    @Provides
    @Singleton
    fun provideTopicSelectApi() : TopicSelectApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL + Constants.BASE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory.create())
            .build()
            .create(TopicSelectApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTopicSelectRepository(
        topicSelectApi: TopicSelectApiService
    ) : TopicSelectRepository {
        return TopicSelectRepositoryImpl(topicSelectApi)
    }

}