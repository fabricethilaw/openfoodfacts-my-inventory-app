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
 * limitations under the License.
 */

package com.thilawfabrice.inventoryapp.repository

import ApiEmptyResponse
import ApiErrorResponse
import ApiResponse
import ApiSuccessResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thilawfabrice.inventoryapp.models.Product
import com.thilawfabrice.inventoryapp.models.toProductDetails
import com.thilawfabrice.inventoryapp.repository.api.ApiController
import com.thilawfabrice.inventoryapp.repository.api.FoodDetails
import com.thilawfabrice.inventoryapp.repository.api.OpenFoodFact

/**
 *  Represents the unique source of true data
 *  the ViewModel will interact with for retrieving and inserting data
 */
class Repository(private val localDB: LocalDatabase) {

    private val apiController = ApiController()
    fun loadSavedProducts() = localDB.productsDao().getAllProducts()
    suspend fun save(product: Product) {
        localDB.productsDao().insertProducts(setOf(product))

    }

    suspend fun resolveProductDetails(code: String): LiveData<FoodDetails?> {
        val rawResponse = apiController.api.getProductDetails(code)
        val liveData = MutableLiveData<FoodDetails?>()
        when (val apiResponse = ApiResponse.create(rawResponse)) {
            is ApiErrorResponse<OpenFoodFact> -> {
                // So there was server error or bad request, etc..
                liveData.postValue(null)
            }
            is ApiEmptyResponse<OpenFoodFact> -> {
                // server returned no useful data
                liveData.postValue(null)
            }

            is ApiSuccessResponse<OpenFoodFact> -> {
                // we are fine
                liveData.postValue(apiResponse.body.toProductDetails())
            }
        }
        return liveData
    }

    fun findProduct(code: String) = localDB.productsDao().findProducts(code)

}