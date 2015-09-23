package com.github.salomonbrys.kotson

import com.google.gson.*

public fun Number.toJson(): JsonPrimitive = JsonPrimitive(this)

public fun Char.toJson(): JsonPrimitive = JsonPrimitive(this)

public fun Boolean.toJson() : JsonPrimitive = JsonPrimitive(this)

public fun String.toJson() : JsonPrimitive = JsonPrimitive(this)

private fun Any?.toJsonElement(): JsonElement {
    if (this == null)
        return JsonNull.INSTANCE

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
