package com.wilinz.androidi18n.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Duration

object OkHttp {
    private val logging = HttpLoggingInterceptor()

    init {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    val client = OkHttpClient.Builder()
        .callTimeout(Duration.ofSeconds(10))
        .addInterceptor(logging)
//        .proxy(Proxy(Proxy.Type.HTTP,InetSocketAddress("127.0.0.1",10809)))
        .connectTimeout(Duration.ofSeconds(10))
        .build()
}