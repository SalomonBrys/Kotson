package com.github.salomonbrys.kotson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


operator fun JsonObject.getValue(thisRef: Any?, property: KProperty<*>): JsonElement = obj[property.name]

operator fun JsonObject.setValue(thisRef: Any?, property: KProperty<*>, value: JsonElement) { obj[property.name] = value }


class JsonObjectDelegate<T>(private val _obj: JsonObject, private val _key: String?, private val _get: (JsonElement) -> T, private val _set: (T) -> JsonElement) : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _get(_obj[_key ?: property.name])
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _obj[_key ?: property.name] = _set(value)
    }

}

class JsonArrayDelegate<T>(private val _array: JsonArray, private val _index: Int, private val _get: (JsonElement) -> T, private val _set: (T) -> JsonElement) : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _get(_array[_index])
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _array[_index] = _set(value)
    }

}

val JsonElement.byString     : JsonObjectDelegate<String>     get() = JsonObjectDelegate(this.obj, null,    { it.string },     { it.toJson() } )
val JsonElement.byBool       : JsonObjectDelegate<Boolean>    get() = JsonObjectDelegate(this.obj, null,    { it.bool },       { it.toJson() } )
val JsonElement.byByte       : JsonObjectDelegate<Byte>       get() = JsonObjectDelegate(this.obj, null,    { it.byte },       { it.toJson() } )
val JsonElement.byChar       : JsonObjectDelegate<Char>       get() = JsonObjectDelegate(this.obj, null,    { it.char },       { it.toJson() } )
val JsonElement.byShort      : JsonObjectDelegate<Short>      get() = JsonObjectDelegate(this.obj, null,    { it.short },      { it.toJson() } )
val JsonElement.byInt        : JsonObjectDelegate<Int>        get() = JsonObjectDelegate(this.obj, null,    { it.int },        { it.toJson() } )
val JsonElement.byLong       : JsonObjectDelegate<Long>       get() = JsonObjectDelegate(this.obj, null,    { it.long },       { it.toJson() } )
val JsonElement.byFloat      : JsonObjectDelegate<Float>      get() = JsonObjectDelegate(this.obj, null,    { it.float },      { it.toJson() } )
val JsonElement.byDouble     : JsonObjectDelegate<Double>     get() = JsonObjectDelegate(this.obj, null,    { it.double },     { it.toJson() } )
val JsonElement.byNumber     : JsonObjectDelegate<Number>     get() = JsonObjectDelegate(this.obj, null,    { it.number },     { it.toJson() } )
val JsonElement.byBigInteger : JsonObjectDelegate<BigInteger> get() = JsonObjectDelegate(this.obj, null,    { it.bigInteger }, { it.toJson() } )
val JsonElement.byBigDecimal : JsonObjectDelegate<BigDecimal> get() = JsonObjectDelegate(this.obj, null,    { it.bigDecimal }, { it.toJson() } )
val JsonElement.byArray      : JsonObjectDelegate<JsonArray>  get() = JsonObjectDelegate(this.obj, null,    { it.array },      { it } )
val JsonElement.byObject     : JsonObjectDelegate<JsonObject> get() = JsonObjectDelegate(this.obj, null,    { it.obj },        { it } )

fun JsonElement.byString(key: String)     = JsonObjectDelegate(this.obj, key, { it.string },     { it.toJson() } )
fun JsonElement.byBool(key: String)       = JsonObjectDelegate(this.obj, key, { it.bool },       { it.toJson() } )
fun JsonElement.byByte(key: String)       = JsonObjectDelegate(this.obj, key, { it.byte },       { it.toJson() } )
fun JsonElement.byChar(key: String)       = JsonObjectDelegate(this.obj, key, { it.char },       { it.toJson() } )
fun JsonElement.byShort(key: String)      = JsonObjectDelegate(this.obj, key, { it.short },      { it.toJson() } )
fun JsonElement.byInt(key: String)        = JsonObjectDelegate(this.obj, key, { it.int },        { it.toJson() } )
fun JsonElement.byLong(key: String)       = JsonObjectDelegate(this.obj, key, { it.long },       { it.toJson() } )
fun JsonElement.byFloat(key: String)      = JsonObjectDelegate(this.obj, key, { it.float },      { it.toJson() } )
fun JsonElement.byDouble(key: String)     = JsonObjectDelegate(this.obj, key, { it.double },     { it.toJson() } )
fun JsonElement.byNumber(key: String)     = JsonObjectDelegate(this.obj, key, { it.number },     { it.toJson() } )
fun JsonElement.byBigInteger(key: String) = JsonObjectDelegate(this.obj, key, { it.bigInteger }, { it.toJson() } )
fun JsonElement.byBigDecimal(key: String) = JsonObjectDelegate(this.obj, key, { it.bigDecimal }, { it.toJson() } )
fun JsonElement.byArray(key: String)      = JsonObjectDelegate(this.obj, key, { it.array },      { it } )
fun JsonElement.byObject(key: String)     = JsonObjectDelegate(this.obj, key, { it.obj },        { it } )

fun JsonElement.byString(index: Int)     = JsonArrayDelegate(this.array, index, { it.string },     { it.toJson() } )
fun JsonElement.byBool(index: Int)       = JsonArrayDelegate(this.array, index, { it.bool },       { it.toJson() } )
fun JsonElement.byByte(index: Int)       = JsonArrayDelegate(this.array, index, { it.byte },       { it.toJson() } )
fun JsonElement.byChar(index: Int)       = JsonArrayDelegate(this.array, index, { it.char },       { it.toJson() } )
fun JsonElement.byShort(index: Int)      = JsonArrayDelegate(this.array, index, { it.short },      { it.toJson() } )
fun JsonElement.byInt(index: Int)        = JsonArrayDelegate(this.array, index, { it.int },        { it.toJson() } )
fun JsonElement.byLong(index: Int)       = JsonArrayDelegate(this.array, index, { it.long },       { it.toJson() } )
fun JsonElement.byFloat(index: Int)      = JsonArrayDelegate(this.array, index, { it.float },      { it.toJson() } )
fun JsonElement.byDouble(index: Int)     = JsonArrayDelegate(this.array, index, { it.double },     { it.toJson() } )
fun JsonElement.byNumber(index: Int)     = JsonArrayDelegate(this.array, index, { it.number },     { it.toJson() } )
fun JsonElement.byBigInteger(index: Int) = JsonArrayDelegate(this.array, index, { it.bigInteger }, { it.toJson() } )
fun JsonElement.byBigDecimal(index: Int) = JsonArrayDelegate(this.array, index, { it.bigDecimal }, { it.toJson() } )
fun JsonElement.byArray(index: Int)      = JsonArrayDelegate(this.array, index, { it.array },      { it } )
fun JsonElement.byObject(index: Int)     = JsonArrayDelegate(this.array, index, { it.obj },        { it } )
