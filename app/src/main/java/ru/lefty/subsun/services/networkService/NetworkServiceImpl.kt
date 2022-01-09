package ru.lefty.subsun.services.networkService

import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkServiceImpl(private val client: OkHttpClient = OkHttpClient()): NetworkService {

    override fun get(url: String): String {
        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string() ?: ""
        }
    }
}