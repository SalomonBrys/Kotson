package com.github.salomonbrys.kotson

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType


inline fun <reified T: Any> gsonTypeToken(): Type  = object : TypeToken<T>() {} .type

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
inline fun <reified T: Any> typeToken(): Type {
    val type = gsonTypeToken<T>()

    if (type is ParameterizedType) {
        if (type.actualTypeArguments.any { it is WildcardType && Object::class.java in it.upperBounds }) {
            if (!type.actualTypeArguments.all { it is WildcardType })
                throw IllegalArgumentException("Either none or all type parameters can be wildcard in $type")
            return type.rawType
        }
    }
    return type
}



fun <T: Any> jsonSerializer(serializer: (arg: SerializerArg<T>) -> JsonElement): JsonSerializer<T>
        = JsonSerializer { src, type, context -> serializer(SerializerArg(src, type, SerializerArg.Context(context))) }

fun <T: Any> jsonDeserializer(deserializer: (arg: DeserializerArg) -> T?): JsonDeserializer<T>
        = JsonDeserializer<T> { json, type, context -> deserializer(DeserializerArg(json, type, DeserializerArg.Context(context))) }

fun <T: Any> instanceCreator(creator: (type: Type) -> T): InstanceCreator<T>
        = InstanceCreator { creator(it) }

fun <T: Any> typeAdapter(readFunction: JsonReader.() -> T, writeFunction: JsonWriter.(value: T) -> Unit)
        = object : TypeAdapter<T>() {
            override fun read(reader: JsonReader) = reader.readFunction()
            override fun write(writer: JsonWriter, value: T) = writer.writeFunction(value)
        }

fun <T: Any> nullableTypeAdapter(readFunction: JsonReader.() -> T?, writeFunction: JsonWriter.(value: T) -> Unit)
        = object : TypeAdapter<T>() {
            override fun read(reader: JsonReader) = reader.readFunction()
            override fun write(writer: JsonWriter, value: T) = writer.writeFunction(value)
        }.nullSafe()


inline fun <reified T: Any> GsonBuilder.registerTypeAdapter(typeAdapter: Any): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), typeAdapter)

inline fun <reified T: Any> GsonBuilder.registerTypeHierarchyAdapter(typeAdapter: Any): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, typeAdapter)


data class SerializerArg<T> (
        val src: T,
        val type: Type,
        val context: SerializerArg.Context
) {
    class Context(val gsonContext: JsonSerializationContext) {
        inline fun <reified T: Any> serialize(src: T) = gsonContext.serialize(src, typeToken<T>())
        fun <T: Any> serialize(src: T, type: Type) = gsonContext.serialize(src, type)
    }
}

data class DeserializerArg (
        val json: JsonElement,
        val type: Type,
        val context: DeserializerArg.Context
) {
    class Context(val gsonContext: JsonDeserializationContext) {
        inline fun <reified T: Any> deserialize(json: JsonElement) = gsonContext.deserialize<T>(json, typeToken<T>())
        fun <T: Any> deserialize(json: JsonElement, type: Type) = gsonContext.deserialize<T>(json, type)
    }
}


sealed class RegistrationBuilder<T: Any, RF>(
        build: RegistrationBuilder<T, RF>.() -> Unit,
        protected val register: (typeAdapter: Any) -> Unit,
        private val _typeAdapterFactory: (RF, JsonWriter.(value: T) -> Unit) -> TypeAdapter<T>
) {

    protected enum class _API { SD, RW }

    private var _api: _API? = null

    private var _readFunction: RF? = null
    private var _writeFunction: (JsonWriter.(value: T) -> Unit)? = null

    private fun _checkApi(api: _API) {
        if (_api != null && _api != api)
            throw IllegalArgumentException("You cannot use serialize/deserialize and read/write for the same type")
        _api = api
    }

    fun serialize(serializer: (arg: SerializerArg<T>) -> JsonElement) {
        _checkApi(_API.SD)
        register(jsonSerializer(serializer))
    }

    fun deserialize(deserializer: (DeserializerArg) -> T?) {
        _checkApi(_API.SD)
        register(jsonDeserializer(deserializer))
    }

    fun createInstances(creator: (type: Type) -> T) = register(instanceCreator(creator))

    private fun _registerTypeAdapter() {
        _checkApi(_API.RW)
        val readFunction = _readFunction
        val writeFunction = _writeFunction
        if (readFunction == null || writeFunction == null)
            return
        register(_typeAdapterFactory(readFunction, writeFunction))
        _readFunction = null
        _writeFunction = null
    }

    fun read(function: RF) {
        _readFunction = function
        _registerTypeAdapter()
    }

    fun write(function: JsonWriter.(value: T) -> Unit) {
        _writeFunction = function
        _registerTypeAdapter()
    }

    init {
        build()
        if (_readFunction != null)
            throw IllegalArgumentException("You cannot define a read function without a write function")
        if (_writeFunction != null)
            throw IllegalArgumentException("You cannot define a write function without a read function")
    }

    class NonNullRegistrationBuilder<T: Any>(build: RegistrationBuilder<T, JsonReader.() -> T>.() -> Unit, register: (typeAdapter: Any) -> Unit)
            : RegistrationBuilder<T, JsonReader.() -> T>(build, register, { rf, wf -> typeAdapter(rf, wf) })

    class NullableRegistrationBuilder<T: Any>(build: RegistrationBuilder<T, JsonReader.() -> T?>.() -> Unit, register: (typeAdapter: Any) -> Unit)
            : RegistrationBuilder<T, JsonReader.() -> T?>(build, register, { rf, wf -> nullableTypeAdapter(rf, wf) })

}


inline fun <reified T: Any> GsonBuilder.registerTypeAdapter(noinline build: RegistrationBuilder<T, JsonReader.() -> T>.() -> Unit): GsonBuilder {
    val type = typeToken<T>()
    RegistrationBuilder.NonNullRegistrationBuilder(build) { registerTypeAdapter(type, it) }
    return this
}

inline fun <reified T: Any> GsonBuilder.registerNullableTypeAdapter(noinline build: RegistrationBuilder<T, JsonReader.() -> T?>.() -> Unit): GsonBuilder {
    val type = typeToken<T>()
    RegistrationBuilder.NullableRegistrationBuilder(build) { registerTypeAdapter(type, it) }
    return this
}

inline fun <reified T: Any> GsonBuilder.registerTypeHierarchyAdapter(noinline build: RegistrationBuilder<T, JsonReader.() -> T>.() -> Unit): GsonBuilder {
    val type = T::class.java
    RegistrationBuilder.NonNullRegistrationBuilder(build) { registerTypeHierarchyAdapter(type, it) }
    return this
}

inline fun <reified T: Any> GsonBuilder.registerNullableTypeHierarchyAdapter(noinline build: RegistrationBuilder<T, JsonReader.() -> T?>.() -> Unit): GsonBuilder {
    val type = T::class.java
    RegistrationBuilder.NullableRegistrationBuilder(build) { registerTypeHierarchyAdapter(type, it) }
    return this
}
