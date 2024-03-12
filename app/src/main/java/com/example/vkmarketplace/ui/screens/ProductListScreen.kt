package com.example.vkmarketplace.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.vkmarketplace.R
import com.example.vkmarketplace.ui.MarketplaceViewModel
import com.example.vkmarketplace.ui.components.MarketplaceSearchBar
import com.example.vkmarketplace.ui.model.Product
import com.example.vkmarketplace.ui.theme.GreenDark
import com.example.vkmarketplace.ui.theme.VKMarketplaceTheme
import kotlin.math.roundToInt

@Composable
internal fun ProductListScreen(
    viewModel: MarketplaceViewModel = hiltViewModel(),
    onProductClick: (String, String) -> Unit
) {
    val lazyProducts = viewModel.products.collectAsLazyPagingItems()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val searchTextState = viewModel.searchTextState
    val state = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MarketplaceSearchBar(
                searchTextState = searchTextState.value,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                },
                onSearchClicked = {
                    viewModel.getSearchProducts(it)
                },
            )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .padding(paddingValues)
        ) {
            Categories(
                categories = categories,
                onClick = { viewModel.getProductsByCategory(it) },
                onCancel = { viewModel.getProducts() }

            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                state = state,
                modifier = Modifier
            ) {
                items(
                    count = lazyProducts.itemCount,
                    key = lazyProducts.itemKey { it.id },
                    contentType = lazyProducts.itemContentType { "contentType" }
                ) { index ->
                    val product = lazyProducts[index]
                    ProductCard(product = product!!, onProductClick)
                }
            }
        }

    }
}


@Composable
fun ProductCard(
    product: Product,
    onProductClick: (String, String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .height(430.dp)
            .clickable { onProductClick(product.title, product.description) }
    ) {
        PagerIndicator(product.images)

        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${product.price} ₽",
                    style = TextStyle(
                        color = GreenDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .alignByBaseline()
                )
                Text(
                    text = "-${product.discountPercentage.roundToInt()} %",
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.alignByBaseline()
                )
            }

            Text(
                text = product.brand,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                text = product.title,
                style = TextStyle(color = Color.Black, fontSize = 20.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Rating(product.rating)
                Text(
                    text = stringResource(R.string.stock, product.stock),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(start = 8.dp),
                    style = TextStyle(fontSize = 14.sp, lineHeight = 12.sp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            onClick = { onProductClick(product.title, product.description) }
        ) {
            Text(
                text = stringResource(R.string.to_cart), modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(), textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun Rating(rating: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "star",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = rating.toString(),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(images: List<String>) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        val pagerState = rememberPagerState(pageCount = {
            images.size
        })
        HorizontalPager(
            modifier = Modifier.fillMaxWidth(),
            state = pagerState,
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(180.dp)
            )
        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.Blue else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Categories(categories: List<String>, onClick: (String) -> Unit, onCancel: () -> Unit) {
    var selected by remember { mutableStateOf("") }

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = {
                    if (selected == category) {
                        selected = ""
                        onCancel()
                    } else {
                        selected = category
                        onClick(category)
                    }
                },
                label = {
                    Text(
                        text = category,
                        fontWeight = if (category == selected) FontWeight.Medium else FontWeight.Normal
                    )
                },
                selected = (category == selected),
                border = FilterChipDefaults.filterChipBorder(
                    borderWidth = 0.5.dp,
                    borderColor = LightGray
                ),
                trailingIcon = {
                    if (selected == category) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon"
                        )
                    }
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProductListScreenPreview() {
    VKMarketplaceTheme {
        ProductListScreen() { _, _ -> }
    }
}

@Preview
@Composable
fun ProductCardPreview() {
    VKMarketplaceTheme {
        ProductCard(
            Product(
                1,
                "iPhone 9",
                "An apple mobile which is nothing like apple",
                549,
                12.96,
                4.69,
                94,
                "Apple",
                "smartphones",
                "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg",
                listOf(
                    "https://cdn.dummyjson.com/product-images/1/1.jpg",
                    "https://cdn.dummyjson.com/product-images/1/2.jpg",
                    "https://cdn.dummyjson.com/product-images/1/3.jpg",
                    "https://cdn.dummyjson.com/product-images/1/4.jpg",
                    "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg"
                )
            ),
        ) { _, _ -> }
    }
}