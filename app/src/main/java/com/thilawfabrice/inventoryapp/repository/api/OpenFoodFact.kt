package com.thilawfabrice.inventoryapp.repository.api

import com.google.gson.annotations.SerializedName

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
	val name: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	)


