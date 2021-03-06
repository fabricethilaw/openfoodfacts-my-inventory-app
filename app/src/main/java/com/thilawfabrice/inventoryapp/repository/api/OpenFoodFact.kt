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

package com.thilawfabrice.inventoryapp.repository.api

import com.google.gson.annotations.SerializedName

/**
 * https://world.openfoodfacts.org/data API model is actually
 * very larger I  kept only the relevant fields for the project
 */
data class OpenFoodFact(

	@field:SerializedName("product")
	val product: FoodDetails? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class FoodDetails(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("product_name")
	val name: String,

	@field:SerializedName("code")
	val code: String? = null,

	)


