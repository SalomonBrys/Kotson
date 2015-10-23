package com.github.salomonbrys.kotson

import com.google.gson.*

public fun Number.toJson(): JsonPrimitive = JsonPrimitive(this)

public fun Char.toJson(): JsonPrimitive = JsonPrimitive(this)

public fun Boolean.toJson() : JsonPrimitive = JsonPrimitive(this)

public fun String.toJson() : JsonPrimitive = JsonPrimitive(this)

internal fun Any?.toJsonElement(): JsonElement {
    if (this == null)
        return jsonNull

    return when (this) {
        is Number -> toJson()
        is Char -> toJson()
        is Boolean -> toJson()
        is String -> toJson()
        is JsonElement -> this
        else -> throw IllegalArgumentException("${this} cannot be converted to JSON")
    }
}

public fun jsonArray(vararg values: Any?): JsonArray {
    val array = JsonArray()
    for (value in values)
        array.add(value.toJsonElement())
    return array;
}

public fun jsonObject(vararg values: Pair<String, Any?>): JsonObject {
    val obj = JsonObject()
    for ((key, value) in values) {
        obj.add(key, value.toJsonElement())
    }
    return obj;
}

public fun JsonObject.shallowCopy(): JsonObject = JsonObject().apply { this@shallowCopy.entrySet().forEach { put(it) } }
public fun JsonArray.shallowCopy(): JsonArray = JsonArray().apply { addAll(this@shallowCopy) }

private fun JsonElement._deepCopy(): JsonElement {
    return when (this) {
        is JsonNull, is JsonPrimitive -> this
        is JsonObject -> deepCopy()
        is JsonArray -> deepCopy()
        else -> throw IllegalArgumentException("Unknown JsonElement: ${this}")
    }
}

public fun JsonObject.deepCopy(): JsonObject = JsonObject().apply { this@deepCopy.entrySet().forEach { add(it.key, it.value._deepCopy()) } }

public fun JsonArray.deepCopy(): JsonArray = JsonArray().apply { this@deepCopy.forEach { add(it._deepCopy()) } }
