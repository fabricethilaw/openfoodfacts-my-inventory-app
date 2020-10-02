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