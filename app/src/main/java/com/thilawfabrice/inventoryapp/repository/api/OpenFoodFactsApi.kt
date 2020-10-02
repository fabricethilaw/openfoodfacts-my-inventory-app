package com.thilawfabrice.inventoryapp.repository.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit.SECONDS


interface OpenFoodFactsApi {
    @GET(BASE_URL + "product/{barcode}.json")
    suspend fun getProductDetails(@Path("barcode") barcode: String): Response<OpenFoodFact>

}

const val BASE_URL = "https://world.openfoodfacts.org/api/v0/"


class ApiController {

    private val client: Retrofit by lazy {

        val gson = GsonBuilder().create()
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(10, SECONDS)
            .readTimeout(10, SECONDS)
            .writeTimeout(10, SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()
    }

    val api: OpenFoodFactsApi = client.create(OpenFoodFactsApi::class.java)
}