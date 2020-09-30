package com.thilawfabrice.inventoryapp.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thilawfabrice.inventoryapp.models.Product
import com.thilawfabrice.inventoryapp.models.ProductDao

/**
 * The [Room] database for this app.
 */
@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)

abstract class LocalDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductDao

    companion object {
        private const val databaseName = "inventory-db"

        fun buildDatabase(context: Context): LocalDatabase {
            return Room.databaseBuilder(context, LocalDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}