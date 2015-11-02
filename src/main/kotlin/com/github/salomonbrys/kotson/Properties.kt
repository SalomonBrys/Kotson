package com.github.salomonbrys.kotson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


public operator fun JsonObject.getValue(thisRef: Any?, property: KProperty<*>): JsonElement = obj[property.name]

public operator fun JsonObject.setValue(thisRef: Any?, property: KProperty<*>, value: JsonElement) { obj[property.name] = value }


public class JsonObjectDelegate<T>(private val _obj: JsonObject, private val _key: String?, private val _get: (JsonElement) -> T, private val _set: (T) -> JsonElement) : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _get(_obj[_key ?: property.name])
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _obj[_key ?: property.name] = _set(value)
    }

}

public class JsonArrayDelegate<T>(private val _array: JsonArray, private val _index: Int, private val _get: (JsonElement) -> T, private val _set: (T) -> JsonElement) : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _get(_array[_index])
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _array[_index] = _set(value)
    }

}

public val JsonElement.byString     : JsonObjectDelegate<String>     get() = JsonObjectDelegate(this.obj, null,    { it.string },     { it.toJson() } )
public val JsonElement.byBool       : JsonObjectDelegate<Boolean>    get() = JsonObjectDelegate(this.obj, null,    { it.bool },       { it.toJson() } )
public val JsonElement.byByte       : JsonObjectDelegate<Byte>       get() = JsonObjectDelegate(this.obj, null,    { it.byte },       { it.toJson() } )
public val JsonElement.byChar       : JsonObjectDelegate<Char>       get() = JsonObjectDelegate(this.obj, null,    { it.char },       { it.toJson() } )
public val JsonElement.byShort      : JsonObjectDelegate<Short>      get() = JsonObjectDelegate(this.obj, null,    { it.short },      { it.toJson() } )
public val JsonElement.byInt        : JsonObjectDelegate<Int>        get() = JsonObjectDelegate(this.obj, null,    { it.int },        { it.toJson() } )
public val JsonElement.byLong       : JsonObjectDelegate<Long>       get() = JsonObjectDelegate(this.obj, null,    { it.long },       { it.toJson() } )
public val JsonElement.byFloat      : JsonObjectDelegate<Float>      get() = JsonObjectDelegate(this.obj, null,    { it.float },      { it.toJson() } )
public val JsonElement.byDouble     : JsonObjectDelegate<Double>     get() = JsonObjectDelegate(this.obj, null,    { it.double },     { it.toJson() } )
public val JsonElement.byNumber     : JsonObjectDelegate<Number>     get() = JsonObjectDelegate(this.obj, null,    { it.number },     { it.toJson() } )
public val JsonElement.byBigInteger : JsonObjectDelegate<BigInteger> get() = JsonObjectDelegate(this.obj, null,    { it.bigInteger }, { it.toJson() } )
public val JsonElement.byBigDecimal : JsonObjectDelegate<BigDecimal> get() = JsonObjectDelegate(this.obj, null,    { it.bigDecimal }, { it.toJson() } )
public val JsonElement.byArray      : JsonObjectDelegate<JsonArray>  get() = JsonObjectDelegate(this.obj, null,    { it.array },      { it } )
public val JsonElement.byObject     : JsonObjectDelegate<JsonObject> get() = JsonObjectDelegate(this.obj, null,    { it.obj },        { it } )

public fun JsonElement.byString(key: String)     = JsonObjectDelegate(this.obj, key,    { it.string },     { it.toJson() } )
public fun JsonElement.byBool(key: String)       = JsonObjectDelegate(this.obj, key,    { it.bool },       { it.toJson() } )
public fun JsonElement.byByte(key: String)       = JsonObjectDelegate(this.obj, key,    { it.byte },       { it.toJson() } )
public fun JsonElement.byChar(key: String)       = JsonObjectDelegate(this.obj, key,    { it.char },       { it.toJson() } )
public fun JsonElement.byShort(key: String)      = JsonObjectDelegate(this.obj, key,    { it.short },      { it.toJson() } )
public fun JsonElement.byInt(key: String)        = JsonObjectDelegate(this.obj, key,    { it.int },        { it.toJson() } )
public fun JsonElement.byLong(key: String)       = JsonObjectDelegate(this.obj, key,    { it.long },       { it.toJson() } )
public fun JsonElement.byFloat(key: String)      = JsonObjectDelegate(this.obj, key,    { it.float },      { it.toJson() } )
public fun JsonElement.byDouble(key: String)     = JsonObjectDelegate(this.obj, key,    { it.double },     { it.toJson() } )
public fun JsonElement.byNumber(key: String)     = JsonObjectDelegate(this.obj, key,    { it.number },     { it.toJson() } )
public fun JsonElement.byBigInteger(key: String) = JsonObjectDelegate(this.obj, key,    { it.bigInteger }, { it.toJson() } )
public fun JsonElement.byBigDecimal(key: String) = JsonObjectDelegate(this.obj, key,    { it.bigDecimal }, { it.toJson() } )
public fun JsonElement.byArray(key: String)      = JsonObjectDelegate(this.obj, key,    { it.array },      { it } )
public fun JsonElement.byObject(key: String)     = JsonObjectDelegate(this.obj, key,    { it.obj },        { it } )

public fun JsonElement.byString(index: Int)     = JsonArrayDelegate(this.array, index, { it.string },     { it.toJson() } )
public fun JsonElement.byBool(index: Int)       = JsonArrayDelegate(this.array, index, { it.bool },       { it.toJson() } )
public fun JsonElement.byByte(index: Int)       = JsonArrayDelegate(this.array, index, { it.byte },       { it.toJson() } )
public fun JsonElement.byChar(index: Int)       = JsonArrayDelegate(this.array, index, { it.char },       { it.toJson() } )
public fun JsonElement.byShort(index: Int)      = JsonArrayDelegate(this.array, index, { it.short },      { it.toJson() } )
public fun JsonElement.byInt(index: Int)        = JsonArrayDelegate(this.array, index, { it.int },        { it.toJson() } )
public fun JsonElement.byLong(index: Int)       = JsonArrayDelegate(this.array, index, { it.long },       { it.toJson() } )
public fun JsonElement.byFloat(index: Int)      = JsonArrayDelegate(this.array, index, { it.float },      { it.toJson() } )
public fun JsonElement.byDouble(index: Int)     = JsonArrayDelegate(this.array, index, { it.double },     { it.toJson() } )
public fun JsonElement.byNumber(index: Int)     = JsonArrayDelegate(this.array, index, { it.number },     { it.toJson() } )
public fun JsonElement.byBigInteger(index: Int) = JsonArrayDelegate(this.array, index, { it.bigInteger }, { it.toJson() } )
public fun JsonElement.byBigDecimal(index: Int) = JsonArrayDelegate(this.array, index, { it.bigDecimal }, { it.toJson() } )
public fun JsonElement.byArray(index: Int)      = JsonArrayDelegate(this.array, index, { it.array },      { it } )
public fun JsonElement.byObject(index: Int)     = JsonArrayDelegate(this.array, index, { it.obj },        { it } )
