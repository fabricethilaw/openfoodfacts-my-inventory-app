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
 *  the UI will interact with for retrieving and inserting data
 */
class Repository(private val localDB: LocalDatabase) {

    private val apiController = ApiController()
    suspend fun loadSavedProducts() = localDB.productsDao().getAllProducts()
    suspend fun saveProduct(product: Product) {
        localDB.productsDao().insertProducts(setOf(product))
    }

    suspend fun resolveProductDetails(reference: String): LiveData<FoodDetails?> {
        val rawResponse = apiController.api.getProductDetails(reference)
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
}