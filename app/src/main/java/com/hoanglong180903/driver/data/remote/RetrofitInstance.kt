package com.hoanglong180903.driver.data.remote

import com.hoanglong180903.driver.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val retrofitService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // Set connection timeout
            .writeTimeout(30, TimeUnit.SECONDS)    // Set write timeout
            .readTimeout(30, TimeUnit.SECONDS)     // Set read timeout
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: ApiService by lazy {
        retrofitService.create(ApiService::class.java)
    }

}