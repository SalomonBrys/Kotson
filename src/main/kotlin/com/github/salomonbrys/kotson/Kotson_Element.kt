package com.github.salomonbrys.kotson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.math.BigDecimal
import java.math.BigInteger

private fun <T : Any> JsonElement?._nullOr(getNotNull: JsonElement.() -> T) : T?
        = if (this == null || isJsonNull) null else getNotNull()

public val JsonElement.string : String get() = asString
public val JsonElement?.nullString : String? get() = _nullOr { string }

public val JsonElement.bool : Boolean get() = asBoolean
public val JsonElement?.nullBool : Boolean? get() = _nullOr { bool }

public val JsonElement.byte : Byte get() = asByte
public val JsonElement?.nullByte : Byte? get() = _nullOr { byte }

public val JsonElement.char : Char get() = asCharacter
public val JsonElement?.nullChar : Char? get() = _nullOr { char }

public val JsonElement.short : Short get() = asShort
public val JsonElement?.nullShort : Short? get() = _nullOr { short }

public val JsonElement.int : Int get() = asInt
public val JsonElement?.nullInt : Int? get() = _nullOr { int }

public val JsonElement.long : Long get() = asLong
public val JsonElement?.nullLong : Long? get() = _nullOr { long }

public val JsonElement.float : Float get() = asFloat
public val JsonElement?.nullFloat : Float? get() = _nullOr { float }

public val JsonElement.double : Double get() = asDouble
public val JsonElement?.nullDouble : Double? get() = _nullOr { double }

public val JsonElement.number : Number get() = asNumber
public val JsonElement?.nullNumber : Number? get() = _nullOr { number }

public val JsonElement.bigInteger : BigInteger get() = asBigInteger
public val JsonElement?.nullBigInteger : BigInteger? get() = _nullOr { bigInteger }

public val JsonElement.bigDecimal : BigDecimal get() = asBigDecimal
public val JsonElement?.nullBigDecimal : BigDecimal? get() = _nullOr { bigDecimal }

public val JsonElement.array : JsonArray get() = asJsonArray
public val JsonElement?.nullArray : JsonArray? get() = _nullOr { array }

public val JsonElement.obj : JsonObject get() = asJsonObject
public val JsonElement?.nullObj : JsonObject? get() = _nullOr { obj }

public fun JsonElement.get(key: String): JsonElement = obj.get(key)
public fun JsonElement.get(index: Int): JsonElement = array.get(index)

public fun JsonElement.contains(key: String): Boolean = obj.has(key)

