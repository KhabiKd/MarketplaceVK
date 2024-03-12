package com.example.vkmarketplace.data

import com.example.vkmarketplace.data.model.ProductRep
import com.example.vkmarketplace.network.model.NetworkProduct

internal fun NetworkProduct.toProductRep(): ProductRep {
    return ProductRep(
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