package com.github.salomonbrys.kotson

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

private fun <T : Any> JsonElement?._nullOr(getNotNull: JsonElement.() -> T) : T?
        = if (this == null || isJsonNull) null else getNotNull()

val JsonElement.string: String get() = asString
val JsonElement?.nullString: String? get() = _nullOr { string }

val JsonElement.bool: Boolean get() = asBoolean
val JsonElement?.nullBool: Boolean? get() = _nullOr { bool }

val JsonElement.byte: Byte get() = asByte
val JsonElement?.nullByte: Byte? get() = _nullOr { byte }

val JsonElement.char: Char get() = asCharacter
val JsonElement?.nullChar: Char? get() = _nullOr { char }

val JsonElement.short: Short get() = asShort
val JsonElement?.nullShort: Short? get() = _nullOr { short }

val JsonElement.int: Int get() = asInt
val JsonElement?.nullInt: Int? get() = _nullOr { int }

val JsonElement.long: Long get() = asLong
val JsonElement?.nullLong: Long? get() = _nullOr { long }

val JsonElement.float: Float get() = asFloat
val JsonElement?.nullFloat: Float? get() = _nullOr { float }

val JsonElement.double: Double get() = asDouble
val JsonElement?.nullDouble: Double? get() = _nullOr { double }

val JsonElement.number: Number get() = asNumber
val JsonElement?.nullNumber: Number? get() = _nullOr { number }

val JsonElement.bigInteger: BigInteger get() = asBigInteger
val JsonElement?.nullBigInteger: BigInteger? get() = _nullOr { bigInteger }

val JsonElement.bigDecimal: BigDecimal get() = asBigDecimal
val JsonElement?.nullBigDecimal: BigDecimal? get() = _nullOr { bigDecimal }

val JsonElement.array: JsonArray get() = asJsonArray
val JsonElement?.nullArray: JsonArray? get() = _nullOr { array }

val JsonElement.obj: JsonObject get() = asJsonObject
val JsonElement?.nullObj: JsonObject? get() = _nullOr { obj }

val jsonNull: JsonNull = JsonNull.INSTANCE

operator fun JsonElement.get(key: String): JsonElement = obj.getNotNull(key)
operator fun JsonElement.get(index: Int): JsonElement = array.get(index)

fun JsonObject.getNotNull(key: String): JsonElement = get(key) ?: throw NoSuchElementException("'$key' is not found")

operator fun JsonObject.contains(key: String): Boolean = has(key)
fun JsonObject.size(): Int = entrySet().size
fun JsonObject.isEmpty(): Boolean = entrySet().isEmpty()
fun JsonObject.isNotEmpty(): Boolean = entrySet().isNotEmpty()
fun JsonObject.keys(): Collection<String> = entrySet().map { it.key }
fun JsonObject.forEach(operation: (String, JsonElement) -> Unit): Unit = entrySet().forEach { operation(it.key, it.value) }
fun JsonObject.toMap(): Map<String, JsonElement> = entrySet().associateBy({ it.key }, { it.value })

operator fun JsonArray.contains(value: Any): Boolean = contains(value.toJsonElement())
