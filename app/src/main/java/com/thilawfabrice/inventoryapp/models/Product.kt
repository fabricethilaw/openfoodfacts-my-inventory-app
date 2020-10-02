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

package com.thilawfabrice.inventoryapp.models

import androidx.lifecycle.LiveData
import androidx.room.*
import com.thilawfabrice.inventoryapp.repository.api.FoodDetails
import com.thilawfabrice.inventoryapp.repository.api.OpenFoodFact

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val reference: String, val name: String, val expiryDate: String, val picture: String
)

fun OpenFoodFact.toProductDetails(): FoodDetails? =
    if (product?.code != null &&
        product.imageUrl != null
    ) {
        product
    } else null

/**
 * Interface for database access for Product  related operations.
 */
@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: Set<Product>)

    @Update(entity = Product::class)
    suspend fun updateProduct(vararg products: Product)

    @Query("SELECT *  FROM products WHERE reference= :ref")
    fun findProducts(ref: String): LiveData<List<Product>>

    @Query("SELECT *  FROM products")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}
