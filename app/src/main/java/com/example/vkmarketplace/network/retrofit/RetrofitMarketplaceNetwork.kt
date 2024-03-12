package com.example.vkmarketplace.network.retrofit

import com.example.vkmarketplace.network.model.NetworkProduct
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitMarketplaceNetworkApi {
    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<NetworkResponse<NetworkProduct>>

    @GET("products/search")
    suspend fun getSearchProducts(
        @Query("q") q: String? = null,
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null
    ): Result<NetworkResponse<NetworkProduct>>

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products/category/{categoryName}")
    suspend fun getProductsByCategory(
        @Path("categoryName") categoryName: String,
        @Query("q") q: String? = null,
        @Query("skip") skip: Int? = null,
        @Query("limit") limit: Int? = null,
    ): Result<NetworkResponse<NetworkProduct>>
}

@Serializable
data class NetworkResponse<T>(
    @SerialName("products") val data: List<T>,
)

fun retrofitMarketplaceNetworkApi(
    baseUrl: String,
    json: Json = Json { ignoreUnknownKeys = true }
): RetrofitMarketplaceNetworkApi  {
    return retrofit(baseUrl, json).create()
}

private fun retrofit(
    baseUrl: String,
    json: Json
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .build()
}
