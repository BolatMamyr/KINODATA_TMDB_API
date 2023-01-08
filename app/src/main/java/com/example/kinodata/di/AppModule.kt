package com.example.kinodata.di

import com.example.kinodata.api.MovieDataApi
import com.example.kinodata.constants.MyConstants.Companion.BASE_URL
import com.example.kinodata.repo.Repository
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
//    val api: MovieDataApi by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(MovieDataApi::class.java)
//    }

    @Provides
    @Singleton
    fun provideApi(): MovieDataApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieDataApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: MovieDataApi): Repository = Repository(api)
}