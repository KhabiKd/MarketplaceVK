package com.example.vkmarketplace.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.vkmarketplace.network.model.NetworkProduct
import com.example.vkmarketplace.network.retrofit.NetworkResponse
import com.example.vkmarketplace.network.retrofit.RetrofitMarketplaceNetworkApi
import retrofit2.HttpException
import java.io.IOException

class MarketplacePagingSource(
    private val retrofitMarketplaceNetworkApi: RetrofitMarketplaceNetworkApi
) : PagingSource<Int, NetworkProduct>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkProduct> {
        val nextPage = params.key ?: 1
        return try {
            val result =
                retrofitMarketplaceNetworkApi.getProducts(skip = (nextPage - 1) * 20, limit = 20)
            if (result.isSuccess) {
                val response: NetworkResponse<NetworkProduct> = result.getOrThrow()

                LoadResult.Page(
                    data = response.data,
                    prevKey = if (nextPage == 1) null else nextPage.minus(1),
                    nextKey = if (response.data.isEmpty()) null else nextPage.plus(1)
                )
            } else {
                return LoadResult.Error(IOException("Network error"))
            }
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NetworkProduct>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}