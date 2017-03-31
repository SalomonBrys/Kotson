package com.github.salomonbrys.kotson

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.stream.JsonToken
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.Reader

inline fun <reified T: Any> Gson.getAdapter(): TypeAdapter<T> = getAdapter(object: TypeToken<T>() {})

inline fun <reified T: Any> Gson.getGenericAdapter(): TypeAdapter<T> = getAdapter(T::class.java)



inline fun <reified T: Any> Gson.fromJson(json: String): T = fromJson(json, typeToken<T>())

inline fun <reified T: Any> Gson.fromJson(json: Reader): T = fromJson(json, typeToken<T>())

inline fun <reified T: Any> Gson.fromJson(json: JsonReader): T = fromJson(json, typeToken<T>())

inline fun <reified T: Any> Gson.fromJson(json: JsonElement): T = fromJson(json, typeToken<T>())



inline fun <reified T: Any> Gson.typedToJson(src: T): String = toJson(src, typeToken<T>())

inline fun <reified T: Any> Gson.typedToJson(src: T, writer: Appendable): Unit = toJson(src, typeToken<T>(), writer)

inline fun <reified T: Any> Gson.typedToJson(src: T, writer: JsonWriter): Unit = toJson(src, typeToken<T>(), writer)

inline fun <reified T: Any> Gson.typedToJsonTree(src: T): JsonElement = toJsonTree(src, typeToken<T>())


// Extensions to JsonReader for reading nullable values

/**
 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER int} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 * If the next token is a string, it will attempt to parse it as an int.
 * If the next token's numeric value cannot be exactly represented by a Java {@code int},
 * this method throws.
 *
 * @throws IllegalStateException if the next token is not a literal value.
 * @throws NumberFormatException if the next literal value is not null but
 *      cannot be parsed as a number, or exactly represented as an int.
 */
fun JsonReader.nextIntOrNull(): Int? {
    if ( this.peek() != JsonToken.NULL ) {
        return this.nextInt()
    } else {
        this.nextNull()
        return null
    }
}

/**
 * Returns the {@link com.google.gson.stream.JsonToken#BOOLEAN boolean} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 *
 * @throws IllegalStateException if the next token is not a boolean or if
 *     this reader is closed.
 */
fun JsonReader.nextBooleanOrNull(): Boolean? {
    if ( this.peek() != JsonToken.NULL ) {
        return this.nextBoolean()
    } else {
        this.nextNull()
        return null
    }
}

/**
 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER double} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 * If the next token is a string, it will attempt to parse it as a double using {@link Double#parseDouble(String)}.
 *
 * @throws IllegalStateException if the next token is not a literal value.
 * @throws NumberFormatException if the next literal value cannot be parsed
 *     as a double, or is non-finite.
 */
fun JsonReader.nextDoubleOrNull(): Double? {
    if ( this.peek() != JsonToken.NULL ) {
        return this.nextDouble()
    } else {
        this.nextNull()
        return null
    }
}

/**
 * Returns the {@link com.google.gson.stream.JsonToken#NUMBER long} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 * If the next token is a string, this method will attempt to parse it as a long.
 * If the next token's numeric value cannot be exactly represented by a Java {@code long}, this method throws.
 *
 * @throws IllegalStateException if the next token is not a literal value.
 * @throws NumberFormatException if the next literal value cannot be parsed
 *     as a number, or exactly represented as a long.
 */
fun JsonReader.nextLongOrNull(): Long? {
    if ( this.peek() != JsonToken.NULL ) {
        return this.nextLong()
    } else {
        this.nextNull()
        return null
    }
}

/**
 * Returns the {@link com.google.gson.stream.JsonToken#STRING string} value of the next token,
 * consuming it. If the next token is {@code NULL}, this method returns {@code null}.
 * If the next token is a number, it will return its string form.
 *
 * @throws IllegalStateException if the next token is not a string or if
 *     this reader is closed.
 */
fun JsonReader.nextStringOrNull(): String? {
    if ( this.peek() != JsonToken.NULL ) {
        return this.nextString()
    } else {
        this.nextNull()
        return null
    }
}
