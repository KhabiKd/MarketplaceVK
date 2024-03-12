package com.example.vkmarketplace.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.vkmarketplace.data.repository.MarketplaceRepository
import com.example.vkmarketplace.ui.model.Product
import com.example.vkmarketplace.ui.model.toProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MarketplaceViewModel @Inject constructor(
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    private val _products = MutableStateFlow<PagingData<Product>>(PagingData.empty())
    val products: StateFlow<PagingData<Product>> get() = _products.asStateFlow()

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> get() = _categories.asStateFlow()

    init {
        getCategories()
        getProducts()
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    private fun getCategories() {
        viewModelScope.launch {
            try {
                _categories.value = marketplaceRepository.getCategories()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            try {
                _products.value = marketplaceRepository.getProducts()
                    .map { pagingData -> pagingData.map { it.toProduct() } }
                    .cachedIn(viewModelScope).first()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSearchProducts(q: String) {
        viewModelScope.launch {
            try {
                _products.value = marketplaceRepository.getSearchProducts(q)
                    .map { pagingData -> pagingData.map { it.toProduct() } }
                    .cachedIn(viewModelScope).first()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getProductsByCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                _products.value = marketplaceRepository.getProductsByCategory(categoryName)
                    .map { pagingData -> pagingData.map { it.toProduct() } }
                    .cachedIn(viewModelScope).first()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}