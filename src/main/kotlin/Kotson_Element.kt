package com.github.salomonbrys.kotson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import java.math.BigDecimal
import java.math.BigInteger

private fun <T : Any> JsonElement?._nullOr(getNotNull: JsonElement.() -> T) : T?
        = if (this == null || isJsonNull()) null else getNotNull()

public val JsonElement.string : String get() = getAsString()
public val JsonElement?.nullString : String? get() = _nullOr { string }

public val JsonElement.bool : Boolean get() = getAsBoolean()
public val JsonElement?.nullBool : Boolean? get() = _nullOr { bool }

public val JsonElement.byte : Byte get() = getAsByte()
public val JsonElement?.nullByte : Byte? get() = _nullOr { byte }

public val JsonElement.char : Char get() = getAsCharacter()
public val JsonElement?.nullChar : Char? get() = _nullOr { char }

public val JsonElement.short : Short get() = getAsShort()
public val JsonElement?.nullShort : Short? get() = _nullOr { short }

public val JsonElement.int : Int get() = getAsInt()
public val JsonElement?.nullInt : Int? get() = _nullOr { int }

public val JsonElement.long : Long get() = getAsLong()
public val JsonElement?.nullLong : Long? get() = _nullOr { long }

public val JsonElement.float : Float get() = getAsFloat()
public val JsonElement?.nullFloat : Float? get() = _nullOr { float }

public val JsonElement.double : Double get() = getAsDouble()
public val JsonElement?.nullDouble : Double? get() = _nullOr { double }

public val JsonElement.number : Number get() = getAsNumber()
public val JsonElement?.nullNumber : Number? get() = _nullOr { number }

public val JsonElement.bigInteger : BigInteger get() = getAsBigInteger()
public val JsonElement?.nullBigInteger : BigInteger? get() = _nullOr { bigInteger }

public val JsonElement.bigDecimal : BigDecimal get() = getAsBigDecimal()
public val JsonElement?.nullBigDecimal : BigDecimal? get() = _nullOr { bigDecimal }

public val JsonElement.array : JsonArray get() = getAsJsonArray()
public val JsonElement?.nullArray : JsonArray? get() = _nullOr { array }

public val JsonElement.obj : JsonObject get() = getAsJsonObject()
public val JsonElement?.nullObj : JsonObject? get() = _nullOr { obj }

public fun JsonElement.get(key: String): JsonElement = obj.get(key)
public fun JsonElement.get(index: Int): JsonElement = array.get(index)

public fun JsonElement.contains(key: String): Boolean = obj.has(key)

