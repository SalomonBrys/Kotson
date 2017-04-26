package com.github.salomonbrys.kotson

import com.google.gson.*
import com.google.gson.stream.JsonWriter

fun Number.toJson(): JsonPrimitive = JsonPrimitive(this)

fun Char.toJson(): JsonPrimitive = JsonPrimitive(this)

fun Boolean.toJson() : JsonPrimitive = JsonPrimitive(this)

fun String.toJson() : JsonPrimitive = JsonPrimitive(this)

internal fun Any?.toJsonElement(): JsonElement {
    if (this == null)
        return jsonNull

    return when (this) {
        is JsonElement -> this
        is String -> toJson()
        is Number -> toJson()
        is Char -> toJson()
        is Boolean -> toJson()
        is Unit -> jsonNull
        else -> throw IllegalArgumentException("${this} cannot be converted to JSON")
    }
}

private fun _jsonArray(values: Iterator<Any?>): JsonArray {
    val array = JsonArray()
    for (value in values)
        array.add(value.toJsonElement())
    return array
}

fun jsonArray(vararg values: Any?) = _jsonArray(values.iterator())
fun jsonArray(values: Iterable<*>) = _jsonArray(values.iterator())
fun jsonArray(values: Sequence<*>) = _jsonArray(values.iterator())

fun Iterable<*>.toJsonArray() = jsonArray(this)
fun Sequence<*>.toJsonArray() = jsonArray(this)

private fun _jsonObject(values: Iterator<Pair<String, *>>): JsonObject {
    val obj = JsonObject()
    for ((key, value) in values) {
        obj.add(key, value.toJsonElement())
    }
    return obj
}

fun jsonObject(vararg values: Pair<String, *>) = _jsonObject(values.iterator())
fun jsonObject(values: Iterable<Pair<String, *>>) = _jsonObject(values.iterator())
fun jsonObject(values: Sequence<Pair<String, *>>) = _jsonObject(values.iterator())

fun Iterable<Pair<String, *>>.toJsonObject() = jsonObject(this)
fun Sequence<Pair<String, *>>.toJsonObject() = jsonObject(this)

fun JsonObject.shallowCopy(): JsonObject = JsonObject().apply { this@shallowCopy.entrySet().forEach { put(it) } }
fun JsonArray.shallowCopy(): JsonArray = JsonArray().apply { addAll(this@shallowCopy) }

private fun JsonElement._deepCopy(): JsonElement {
    return when (this) {
        is JsonNull, is JsonPrimitive -> this
        is JsonObject -> deepCopy()
        is JsonArray -> deepCopy()
        else -> throw IllegalArgumentException("Unknown JsonElement: ${this}")
    }
}

fun JsonObject.deepCopy(): JsonObject = JsonObject().apply { this@deepCopy.entrySet().forEach { add(it.key, it.value._deepCopy()) } }

fun JsonArray.deepCopy(): JsonArray = JsonArray().apply { this@deepCopy.forEach { add(it._deepCopy()) } }

fun JsonWriter.value(value: Any) : JsonWriter {
    return when (value) {
        is Boolean -> value(value)
        is Double -> value(value)
        is Long -> value(value)
        is Number -> value(value)
        is String -> value(value)
        else -> throw IllegalArgumentException("${this} cannot be written on JsonWriter")
    }
}
