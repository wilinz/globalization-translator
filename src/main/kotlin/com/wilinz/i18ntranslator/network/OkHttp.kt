package com.wilinz.i18ntranslator.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Duration

object OkHttp {
    private val logging = HttpLoggingInterceptor()

    init {
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
    }

    val client = OkHttpClient.Builder()
        .addInterceptor {
            val newRequest = it.request().newBuilder()
                .header("User-Agent", HttpData.userAgent)
                .build()
            return@addInterceptor it.proceed(newRequest)
        }
        .callTimeout(Duration.ofSeconds(10))
        .addInterceptor(logging)
        .connectTimeout(Duration.ofSeconds(10))
        .build()
}