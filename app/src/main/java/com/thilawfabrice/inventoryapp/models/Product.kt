package com.thilawfabrice.inventoryapp.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val reference: String, val name: String, val expireDate: String, val picture: String
)

/**
 * Interface for database access for Product  related operations.
 */
@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: Set<Product>)

    @Query("SELECT *  FROM products WHERE reference= :ref")
    fun findProducts(ref: String): LiveData<List<Product>>


    @Query("DELETE FROM products")
    suspend fun deleteAll()
}