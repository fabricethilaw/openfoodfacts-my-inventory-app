package com.thilawfabrice.inventoryapp.repository

import com.thilawfabrice.inventoryapp.models.Product

/**
 *  Represents the unique source of true data
 *  the UI will interact with for retrieving and inserting data
 */
class Repository(private val localDB: LocalDatabase) {

    suspend fun loadSavedProducts() = localDB.productsDao().getAllProducts()
    suspend fun saveProduct(product: Product) {
        localDB.productsDao().insertProducts(setOf(product))
    }

    // todo verify reference online before saving it in local db


}