package ru.lefty.subsun.utils.serializers

interface Serializer<T> {

    fun toJson(value: T): String

    fun fromJson(json: String): T
}
