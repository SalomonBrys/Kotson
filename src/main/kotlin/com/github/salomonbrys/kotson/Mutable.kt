package com.github.salomonbrys.kotson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject



operator fun JsonElement.set(key: String, value: Any?) = obj.add(key, value.toJsonElement())
operator fun JsonElement.set(key: Int, value: Any?) = array.set(key, value.toJsonElement())



fun JsonObject.put(pair: Pair<String, Any?>) = add(pair.first, pair.second.toJsonElement())
fun JsonObject.put(entry: Map.Entry<String, Any?>) = add(entry.key, entry.value.toJsonElement())

fun JsonObject.putAll(vararg pairs: Pair<String, Any?>) = pairs.forEach { put(it) }
fun JsonObject.putAll(vararg entries: Map.Entry<String, Any?>) = entries.forEach { put(it) }
fun JsonObject.putAll(map: Map<String, Any?>) = map.entries.forEach { put(it) }
fun JsonObject.putAll(obj: JsonObject) = obj.entrySet().forEach { put(it) }
fun JsonObject.putAll(pairs: Sequence<Pair<String, Any?>>) = pairs.forEach { put(it) }
fun JsonObject.putAll(pairs: Iterable<Pair<String, Any?>>) = pairs.forEach { put(it) }
fun JsonObject.putAllEntries(entries: Sequence<Map.Entry<String, Any?>>) = entries.forEach { put(it) }
fun JsonObject.putAllEntries(entries: Iterable<Map.Entry<String, Any?>>) = entries.forEach { put(it) }

operator fun JsonObject.plus(pair: Pair<String, Any?>) = shallowCopy().apply { put(pair) }
operator fun JsonObject.plus(entry: Map.Entry<String, Any?>) = shallowCopy().apply { put(entry) }
operator fun JsonObject.plus(pairs: Array<Pair<String, Any?>>) = shallowCopy().apply { putAll(*pairs) }
operator fun JsonObject.plus(entries: Array<Map.Entry<String, Any?>>) = shallowCopy().apply { putAll(*entries) }
operator fun JsonObject.plus(map: Map<String, Any?>) = shallowCopy().apply { putAll(map) }
operator fun JsonObject.plus(obj: JsonObject) = shallowCopy().apply { putAll(obj) }

operator fun JsonObject.plusAssign(pair: Pair<String, Any?>) = put(pair)
operator fun JsonObject.plusAssign(entry: Map.Entry<String, Any?>) = put(entry)
operator fun JsonObject.plusAssign(pairs: Array<Pair<String, Any?>>) = putAll(*pairs)
operator fun JsonObject.plusAssign(entries: Array<Map.Entry<String, Any?>>) = putAll(*entries)
operator fun JsonObject.plusAssign(map: Map<String, Any?>) = putAll(map)
operator fun JsonObject.plusAssign(obj: JsonObject) = putAll(obj)

fun JsonObject.removeAll(vararg keys: String) = keys.forEach { remove(it) }
fun JsonObject.removeAll(keys: Iterable<String>) = keys.forEach { remove(it) }
fun JsonObject.removeAll(keys: Sequence<String>) = keys.forEach { remove(it) }
fun JsonObject.removeAllJsonKeys(vararg keys: JsonElement) = keys.forEach { remove(it.string) }
fun JsonObject.removeAllJsonKeys(keys: Iterable<JsonElement>) = keys.forEach { remove(it.string) }
fun JsonObject.removeAllJsonKeys(keys: Sequence<JsonElement>) = keys.forEach { remove(it.string) }

operator fun JsonObject.minus(key: String) = shallowCopy().apply { remove(key) }
operator fun JsonObject.minus(keys: Array<String>) = shallowCopy().apply { removeAll(*keys) }
operator fun JsonObject.minus(keys: Iterable<String>) = shallowCopy().apply { removeAll(keys) }
operator fun JsonObject.minus(keys: Sequence<String>) = shallowCopy().apply { removeAll(keys) }

operator fun JsonObject.minusAssign(key: String) { remove(key) }
operator fun JsonObject.minusAssign(keys: Array<String>) = removeAll(*keys)
operator fun JsonObject.minusAssign(keys: Iterable<String>) = removeAll(keys)
operator fun JsonObject.minusAssign(keys: Sequence<String>) = removeAll(keys)



fun JsonArray.add(value: Any?) = add(value.toJsonElement())

fun JsonArray.addAll(vararg values: Any?) = values.forEach { add(it) }
fun JsonArray.addAll(values: Iterable<Any?>) = values.forEach { add(it) }
fun JsonArray.addAll(values: Sequence<Any?>) = values.forEach { add(it) }

operator fun JsonArray.plus(value: Any?) = shallowCopy().apply { add(value) }
operator fun JsonArray.plus(values: Array<Any?>) = shallowCopy().apply { addAll(values) }
operator fun JsonArray.plus(values: Iterable<Any?>) = shallowCopy().apply { addAll(values) }
operator fun JsonArray.plus(values: Sequence<Any?>) = shallowCopy().apply { addAll(values) }

operator fun JsonArray.plusAssign(value: Any?) = add(value)
operator fun JsonArray.plusAssign(values: Array<Any?>) = addAll(values)
operator fun JsonArray.plusAssign(values: Iterable<Any?>) = addAll(values)
operator fun JsonArray.plusAssign(values: Sequence<Any?>) = addAll(values)

fun JsonArray.removeAll(vararg values: JsonElement) = values.forEach { remove(it) }
fun JsonArray.removeAll(values: Iterable<JsonElement>) = values.forEach { remove(it) }
fun JsonArray.removeAll(values: Sequence<JsonElement>) = values.forEach { remove(it) }

operator fun JsonArray.minus(value: JsonElement) = shallowCopy().apply { remove(value) }
operator fun JsonArray.minus(values: Array<JsonElement>) = shallowCopy().apply { removeAll(*values) }
operator fun JsonArray.minus(values: Iterable<JsonElement>) = shallowCopy().apply { removeAll(values) }
operator fun JsonArray.minus(values: Sequence<JsonElement>) = shallowCopy().apply { removeAll(values) }

operator fun JsonArray.minusAssign(value: JsonElement) { remove(value) }
operator fun JsonArray.minusAssign(values: Array<JsonElement>) = removeAll(*values)
operator fun JsonArray.minusAssign(values: Iterable<JsonElement>) = removeAll(values)
operator fun JsonArray.minusAssign(values: Sequence<JsonElement>) = removeAll(values)

fun JsonArray.removeAllIndexes(vararg indexes: Int) = indexes.forEach { remove(it) }
fun JsonArray.removeAllIndexes(indexes: Iterable<Int>) = indexes.forEach { remove(it) }
fun JsonArray.removeAllIndexes(indexes: Sequence<Int>) = indexes.forEach { remove(it) }
