//package com.aadityaverma.remotify.data.datasource
//
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitBuild{
//    private const val BASE_URL = "https://api.spotify.com/"
//
//    private val logging = HttpLoggingInterceptor().apply{
//        setLevel(HttpLoggingInterceptor.Level.BODY)
//    }
//    private val httpClient = OkHttpClient.Builder()
//        .addInterceptor(logging)
//        .build()
//    val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(httpClient)
//        .build()
//
//}