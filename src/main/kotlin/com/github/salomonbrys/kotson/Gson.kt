package com.github.salomonbrys.kotson

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.Reader

public inline fun <reified T: Any> Gson.getAdapter(): TypeAdapter<T> = getAdapter(object: TypeToken<T>() {})

public inline fun <reified T: Any> Gson.getGenericAdapter(): TypeAdapter<T> = getAdapter(T::class.java)



public inline fun <reified T: Any> Gson.fromJson(json: String): T = fromJson(json, typeToken<T>())

public inline fun <reified T: Any> Gson.fromJson(json: Reader): T = fromJson(json, typeToken<T>())

public inline fun <reified T: Any> Gson.fromJson(json: JsonReader): T = fromJson(json, typeToken<T>())

public inline fun <reified T: Any> Gson.fromJson(json: JsonElement): T = fromJson(json, typeToken<T>())



public inline fun <reified T: Any> Gson.typedToJson(src: T): String = toJson(src, typeToken<T>())

public inline fun <reified T: Any> Gson.typedToJson(src: T, writer: Appendable): Unit = toJson(src, typeToken<T>(), writer)

public inline fun <reified T: Any> Gson.typedToJson(src: T, writer: JsonWriter): Unit = toJson(src, typeToken<T>(), writer)

public inline fun <reified T: Any> Gson.typedToJsonTree(src: T): JsonElement = toJsonTree(src, typeToken<T>())
