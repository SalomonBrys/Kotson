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


public class JsonDelegate<T>(public val obj: JsonObject, private val _get: (JsonElement) -> T, private val _set: (T) -> JsonElement) : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _get(obj[property.name])
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        obj[property.name] = _set(value)
    }

}

public val JsonObject.byString: JsonDelegate<String>         get() = JsonDelegate(this, { it.string }, { it.toJson() } )
public val JsonObject.byBool: JsonDelegate<Boolean>          get() = JsonDelegate(this, { it.bool }, { it.toJson() } )
public val JsonObject.byByte: JsonDelegate<Byte>             get() = JsonDelegate(this, { it.byte }, { it.toJson() } )
public val JsonObject.byChar: JsonDelegate<Char>             get() = JsonDelegate(this, { it.char }, { it.toJson() } )
public val JsonObject.byShort: JsonDelegate<Short>           get() = JsonDelegate(this, { it.short }, { it.toJson() } )
public val JsonObject.byInt: JsonDelegate<Int>               get() = JsonDelegate(this, { it.int }, { it.toJson() } )
public val JsonObject.byLong: JsonDelegate<Long>             get() = JsonDelegate(this, { it.long }, { it.toJson() } )
public val JsonObject.byFloat: JsonDelegate<Float>           get() = JsonDelegate(this, { it.float }, { it.toJson() } )
public val JsonObject.byDouble: JsonDelegate<Double>         get() = JsonDelegate(this, { it.double }, { it.toJson() } )
public val JsonObject.byNumber: JsonDelegate<Number>         get() = JsonDelegate(this, { it.number }, { it.toJson() } )
public val JsonObject.byBigInteger: JsonDelegate<BigInteger> get() = JsonDelegate(this, { it.bigInteger }, { it.toJson() } )
public val JsonObject.byBigDecimal: JsonDelegate<BigDecimal> get() = JsonDelegate(this, { it.bigDecimal }, { it.toJson() } )
public val JsonObject.byArray: JsonDelegate<JsonArray>       get() = JsonDelegate(this, { it.array }, { it } )
public val JsonObject.byObject: JsonDelegate<JsonObject>     get() = JsonDelegate(this, { it.obj }, { it } )

