package com.github.salomonbrys.kotson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject



operator public fun JsonElement.set(key: String, value: Any?) = obj.add(key, value.toJsonElement())
operator public fun JsonElement.set(key: Int, value: Any?) = array.set(key, value.toJsonElement())



public fun JsonObject.put(pair: Pair<String, Any?>) = add(pair.first, pair.second.toJsonElement())
public fun JsonObject.put(entry: Map.Entry<String, Any?>) = add(entry.getKey(), entry.getValue().toJsonElement())

public fun JsonObject.putAll(vararg pairs: Pair<String, Any?>) = pairs.forEach { put(it) }
public fun JsonObject.putAll(vararg entries: Map.Entry<String, Any?>) = entries.forEach { put(it) }
public fun JsonObject.putAll(map: Map<String, Any?>) = map.entrySet().forEach { put(it) }
public fun JsonObject.putAll(obj: JsonObject) = obj.entrySet().forEach { put(it) }
public fun JsonObject.putAll(pairs: Sequence<Pair<String, Any?>>) = pairs.forEach { put(it) }
public fun JsonObject.putAll(pairs: Iterable<Pair<String, Any?>>) = pairs.forEach { put(it) }
public fun JsonObject.putAllEntries(entries: Sequence<Map.Entry<String, Any?>>) = entries.forEach { put(it) }
public fun JsonObject.putAllEntries(entries: Iterable<Map.Entry<String, Any?>>) = entries.forEach { put(it) }

operator public fun JsonObject.plus(pair: Pair<String, Any?>) = shallowCopy().apply { put(pair) }
operator public fun JsonObject.plus(entry: Map.Entry<String, Any?>) = shallowCopy().apply { put(entry) }
operator public fun JsonObject.plus(pairs: Array<Pair<String, Any?>>) = shallowCopy().apply { putAll(*pairs) }
operator public fun JsonObject.plus(entries: Array<Map.Entry<String, Any?>>) = shallowCopy().apply { putAll(*entries) }
operator public fun JsonObject.plus(map: Map<String, Any?>) = shallowCopy().apply { putAll(map) }
operator public fun JsonObject.plus(obj: JsonObject) = shallowCopy().apply { putAll(obj) }

operator public fun JsonObject.plusAssign(pair: Pair<String, Any?>) = put(pair)
operator public fun JsonObject.plusAssign(entry: Map.Entry<String, Any?>) = put(entry)
operator public fun JsonObject.plusAssign(pairs: Array<Pair<String, Any?>>) = putAll(*pairs)
operator public fun JsonObject.plusAssign(entries: Array<Map.Entry<String, Any?>>) = putAll(*entries)
operator public fun JsonObject.plusAssign(map: Map<String, Any?>) = putAll(map)
operator public fun JsonObject.plusAssign(obj: JsonObject) = putAll(obj)

public fun JsonObject.removeAll(vararg keys: String) = keys.forEach { remove(it) }
public fun JsonObject.removeAll(keys: Iterable<String>) = keys.forEach { remove(it) }
public fun JsonObject.removeAll(keys: Sequence<String>) = keys.forEach { remove(it) }
public fun JsonObject.removeAllJsonKeys(vararg keys: JsonElement) = keys.forEach { remove(it.string) }
public fun JsonObject.removeAllJsonKeys(keys: Iterable<JsonElement>) = keys.forEach { remove(it.string) }
public fun JsonObject.removeAllJsonKeys(keys: Sequence<JsonElement>) = keys.forEach { remove(it.string) }

operator public fun JsonObject.minus(key: String) = shallowCopy().apply { remove(key) }
operator public fun JsonObject.minus(keys: Array<String>) = shallowCopy().apply { removeAll(*keys) }
operator public fun JsonObject.minus(keys: Iterable<String>) = shallowCopy().apply { removeAll(keys) }
operator public fun JsonObject.minus(keys: Sequence<String>) = shallowCopy().apply { removeAll(keys) }

operator public fun JsonObject.minusAssign(key: String) = remove(key)
operator public fun JsonObject.minusAssign(keys: Array<String>) = removeAll(*keys)
operator public fun JsonObject.minusAssign(keys: Iterable<String>) = removeAll(keys)
operator public fun JsonObject.minusAssign(keys: Sequence<String>) = removeAll(keys)



public fun JsonArray.add(value: Any?) = add(value.toJsonElement())

public fun JsonArray.addAll(vararg values: Any?) = values.forEach { add(it) }
public fun JsonArray.addAll(values: Iterable<Any?>) = values.forEach { add(it) }
public fun JsonArray.addAll(values: Sequence<Any?>) = values.forEach { add(it) }

operator public fun JsonArray.plus(value: Any?) = shallowCopy().apply { add(value) }
operator public fun JsonArray.plus(vararg values: Any?) = shallowCopy().apply { addAll(values) }
operator public fun JsonArray.plus(values: Iterable<Any?>) = shallowCopy().apply { addAll(values) }
operator public fun JsonArray.plus(values: Sequence<Any?>) = shallowCopy().apply { addAll(values) }

operator public fun JsonArray.plusAssign(value: Any?) = add(value)
operator public fun JsonArray.plusAssign(vararg values: Any?) = addAll(values)
operator public fun JsonArray.plusAssign(values: Iterable<Any?>) = addAll(values)
operator public fun JsonArray.plusAssign(values: Sequence<Any?>) = addAll(values)

public fun JsonArray.removeAll(vararg values: JsonElement) = values.forEach { remove(it) }
public fun JsonArray.removeAll(values: Iterable<JsonElement>) = values.forEach { remove(it) }
public fun JsonArray.removeAll(values: Sequence<JsonElement>) = values.forEach { remove(it) }

operator public fun JsonArray.minus(value: JsonElement) = shallowCopy().apply { remove(value) }
operator public fun JsonArray.minus(vararg values: JsonElement) = shallowCopy().apply { removeAll(*values) }
operator public fun JsonArray.minus(values: Iterable<JsonElement>) = shallowCopy().apply { removeAll(values) }
operator public fun JsonArray.minus(values: Sequence<JsonElement>) = shallowCopy().apply { removeAll(values) }

operator public fun JsonArray.minusAssign(value: JsonElement) = remove(value)
operator public fun JsonArray.minusAssign(vararg values: JsonElement) = removeAll(*values)
operator public fun JsonArray.minusAssign(values: Iterable<JsonElement>) = removeAll(values)
operator public fun JsonArray.minusAssign(values: Sequence<JsonElement>) = removeAll(values)

public fun JsonArray.removeAllIndexes(vararg indexes: Int) = indexes.forEach { remove(it) }
public fun JsonArray.removeAllIndexes(indexes: Iterable<Int>) = indexes.forEach { remove(it) }
public fun JsonArray.removeAllIndexes(indexes: Sequence<Int>) = indexes.forEach { remove(it) }
