package com.example.vkmarketplace.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.vkmarketplace.data.model.ProductRep
import com.example.vkmarketplace.data.paging.CategoriesPagingSource
import com.example.vkmarketplace.data.paging.MarketplacePagingSource
import com.example.vkmarketplace.data.paging.SearchPagingSource
import com.example.vkmarketplace.data.toProductRep
import com.example.vkmarketplace.network.retrofit.RetrofitMarketplaceNetworkApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface MarketplaceRepository {
    fun getProducts(): Flow<PagingData<ProductRep>>

    fun getSearchProducts(q: String): Flow<PagingData<ProductRep>>

    suspend fun getCategories(): List<String>

    fun getProductsByCategory(categoryName: String): Flow<PagingData<ProductRep>>
}

class MarketplaceRepositoryImpl @Inject constructor(
    private val retrofitMarketplaceNetworkApi: RetrofitMarketplaceNetworkApi
): MarketplaceRepository {
    override fun getProducts(): Flow<PagingData<ProductRep>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { MarketplacePagingSource(retrofitMarketplaceNetworkApi) }
        ).flow.map { pagingData -> pagingData.map { it.toProductRep() } }
    }

    override fun getSearchProducts(q: String): Flow<PagingData<ProductRep>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { SearchPagingSource(retrofitMarketplaceNetworkApi, q) }
        ).flow.map { pagingData -> pagingData.map { it.toProductRep() } }
    }

    override suspend fun getCategories(): List<String> {
        return retrofitMarketplaceNetworkApi.getCategories()
    }

    override fun getProductsByCategory(categoryName: String): Flow<PagingData<ProductRep>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { CategoriesPagingSource(retrofitMarketplaceNetworkApi, categoryName) }
        ).flow.map { pagingData -> pagingData.map { it.toProductRep() } }
    }
}