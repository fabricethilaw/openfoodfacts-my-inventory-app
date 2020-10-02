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