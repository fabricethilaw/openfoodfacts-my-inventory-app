package com.thilawfabrice.inventoryapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thilawfabrice.inventoryapp.models.Product
import com.thilawfabrice.inventoryapp.repository.Repository
import com.thilawfabrice.inventoryapp.repository.api.FoodDetails
import com.thilawfabrice.inventoryapp.views.ProductViewItem
import com.thilawfabrice.inventoryapp.views.asViewItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: Repository,
    val otherProperty: Any
) : ViewModel() {
    companion object {
        /**
         * Factory for creating [ProductViewModel]
         * Pass repository to [ProductViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::ProductViewModel)
    }

    suspend fun getProductList(): LiveData<List<ProductViewItem>> {
        val rawData = repository.loadSavedProducts()
        val result = MutableLiveData<List<ProductViewItem>>()
        with(rawData.hasActiveObservers().not()) {
            rawData.observeForever {
                result.postValue(it.asViewItems())
            }
        }
        return result
    }


    suspend fun checkReferenceOnline(code: String): LiveData<FoodDetails?> {
        return repository.resolveProductDetails(code)
    }

    // todo()
    fun saveProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.saveProduct(product)
        }
    }


}