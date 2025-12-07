package com.escom.mediafinder.di

import android.content.Context
import androidx.room.Room
import com.escom.mediafinder.data.local.MediaFinderDatabase
import com.escom.mediafinder.data.remote.TVMazeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideTVMazeApi(okHttpClient: OkHttpClient): TVMazeApi {
        return Retrofit.Builder()
            .baseUrl("https://api.tvmaze.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TVMazeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MediaFinderDatabase {
        return Room.databaseBuilder(
            context,
            MediaFinderDatabase::class.java,
            "mediafinder_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: MediaFinderDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(database: MediaFinderDatabase) = database.favoriteDao()

    @Provides
    @Singleton
    fun provideSearchHistoryDao(database: MediaFinderDatabase) = database.searchHistoryDao()

    @Provides
    @Singleton
    fun provideShowCacheDao(database: MediaFinderDatabase) = database.showCacheDao()
}