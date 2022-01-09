package ru.lefty.subsun.services.networkService

import okhttp3.Request

interface NetworkService {

    fun get(url: String): String
}