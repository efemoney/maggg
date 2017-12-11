package com.efemoney.maggg.interceptor;

import com.efemoney.maggg.inject.qualifier.ApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor
@Inject constructor(@ApiKey val apiKey: String)
    : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response? {

        val req = chain.request()

        val url = req.url()
                .newBuilder()
                .setQueryParameter("api_key", apiKey)
                .build()

        val newReq = req
                .newBuilder()
                .url(url)
                .build()

        return chain.proceed(newReq)
    }
}
