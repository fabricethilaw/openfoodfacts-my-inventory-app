/*
 * Copyright (C) 2020 Fabrice Thilaw (@thilawfab)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.*/

package com.thilawfabrice.inventoryapp.viewmodels

import androidx.lifecycle.*
import com.thilawfabrice.inventoryapp.formatDate
import com.thilawfabrice.inventoryapp.formatToDate
import com.thilawfabrice.inventoryapp.models.Product
import com.thilawfabrice.inventoryapp.repository.Repository
import com.thilawfabrice.inventoryapp.repository.api.FoodDetails
import com.thilawfabrice.inventoryapp.takeOldest
import com.thilawfabrice.inventoryapp.views.ProductViewItem
import com.thilawfabrice.inventoryapp.views.asViewItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductViewModel(
    private val repository: Repository,
    private val lifecycleOwner: LifecycleOwner
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
        if (rawData.hasActiveObservers().not()) {
            rawData.observe(owner = lifecycleOwner) { data ->
                result.postValue(data.asViewItems())
            }
        }
        return result
    }


    suspend fun checkReferenceOnline(code: String): LiveData<FoodDetails?> {
        return repository.resolveProductDetails(code)
    }

    fun saveProduct(validData: FoodDetails, newExpiryDate: String) {
        viewModelScope.launch(Dispatchers.Main)
        {
            with(validData) {
                repository.findProduct(validData.code!!).also { lookForAlreadySavedProduct ->

                    lookForAlreadySavedProduct.observe(owner = lifecycleOwner) { matches ->
                        // Save new product if no match
                        if (matches.isEmpty()) {

                            viewModelScope.launch(Dispatchers.Main) {
                                val product = Product(
                                    reference = code!!,
                                    name = name,
                                    picture = imageUrl!!,
                                    expiryDate = newExpiryDate
                                )
                                // stop observing to prevent looping
                                lookForAlreadySavedProduct.removeObservers(lifecycleOwner)
                                repository.save(product)
                            }

                        } else {
                            // otherwise update existing product
                            // stop observing to prevent looping
                            lookForAlreadySavedProduct.removeObservers(lifecycleOwner)

                            update(
                                newExpiryDate = newExpiryDate,
                                previousProduct = matches.first()
                            )
                        }
                    }
                }

            }

        }
    }


    private fun update(
        previousProduct: Product,
        newExpiryDate: String
    ) {
        //otherwise  only update the old one

        with(previousProduct) {
            // make a  copy of the old product with the closest date
            val newDate = newExpiryDate.formatToDate()
            val oldDate = expiryDate.formatToDate()

            if (oldDate != null && newDate != null) {

                val update = copy(expiryDate = oldDate.takeOldest(newDate).formatDate())
                // then update older record
                viewModelScope.launch(Dispatchers.Main) {
                    repository.save(update)
                }
            }
        }
    }
}
