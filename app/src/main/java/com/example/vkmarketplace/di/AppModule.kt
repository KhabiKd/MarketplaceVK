package com.example.vkmarketplace.di

import com.example.vkmarketplace.BuildConfig
import com.example.vkmarketplace.data.repository.MarketplaceRepository
import com.example.vkmarketplace.data.repository.MarketplaceRepositoryImpl
import com.example.vkmarketplace.network.retrofit.RetrofitMarketplaceNetworkApi
import com.example.vkmarketplace.network.retrofit.retrofitMarketplaceNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMarketplaceApi(): RetrofitMarketplaceNetworkApi {
        return retrofitMarketplaceNetworkApi(baseUrl = BuildConfig.MARKETPLACE_BASE_URL)
    }

    @Provides
    @Singleton
    fun provideMarketplaceRepository(retrofitMarketplaceNetworkApi: RetrofitMarketplaceNetworkApi): MarketplaceRepository {
        return MarketplaceRepositoryImpl(retrofitMarketplaceNetworkApi)
    }
}