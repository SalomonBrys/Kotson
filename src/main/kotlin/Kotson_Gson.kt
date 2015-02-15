package com.github.salomonbrys.kotson

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.JsonElement
import com.google.gson.stream.JsonWriter
import java.io.Reader
import com.google.gson.stream.JsonReader

public inline fun <reified T: Any> Gson.getAdapter(): TypeAdapter<T>
        = this.getAdapter(object: TypeToken<T>() {})

public inline fun <reified T: Any> Gson.fromJson(json: String): T?
        = this.fromJson(json, typeToken<T>())

public inline fun <reified T: Any> Gson.fromJson(json: Reader): T?
        = this.fromJson(json, typeToken<T>())

public inline fun <reified T: Any> Gson.fromJson(json: JsonReader): T?
        = this.fromJson(json, typeToken<T>())

public inline fun <reified T: Any> Gson.fromJson(json: JsonElement): T?
        = this.fromJson(json, typeToken<T>())
