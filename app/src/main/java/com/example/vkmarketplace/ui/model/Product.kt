package com.example.vkmarketplace.ui.model

import com.example.vkmarketplace.data.model.ProductRep
import com.example.vkmarketplace.network.model.NetworkProduct
import javax.annotation.concurrent.Immutable

@Immutable
data class Product (
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

internal fun ProductRep.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        brand = brand,
        category = category,
        thumbnail = thumbnail,
        images = images
    )
}