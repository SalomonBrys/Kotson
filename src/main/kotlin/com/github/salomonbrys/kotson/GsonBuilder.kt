package com.github.salomonbrys.kotson

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType


public inline fun <reified T: Any> typeToken(): Type {
    val type = object : TypeToken<T>() {} .type

    if (type is ParameterizedType) {
        if (type.actualTypeArguments.any { it is WildcardType }) {
            if (!type.actualTypeArguments.all { it is WildcardType })
                throw IllegalArgumentException("Either none or all type parameters can be wildcard in ${type}")
            return type.rawType
        }
    }
    return type
}



public fun <T: Any> jsonSerializer(serializer: (arg: SerializerArg<T>) -> JsonElement): JsonSerializer<T>
        = JsonSerializer { src, type, context -> serializer(SerializerArg(src, type, SerializerArg.Context(context))) }

public fun <T: Any> jsonDeserializer(deserializer: (arg: DeserializerArg) -> T?): JsonDeserializer<T>
        = JsonDeserializer<T> { json, type, context -> deserializer(DeserializerArg(json, type, DeserializerArg.Context(context))) }

public fun <T: Any> instanceCreator(creator: (type: Type) -> T): InstanceCreator<T>
        = InstanceCreator { creator(it) }



public inline fun <reified T: Any> GsonBuilder.registerTypeAdapter(typeAdapter: Any): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), typeAdapter)

public inline fun <reified T: Any> GsonBuilder.registerTypeHierarchyAdapter(typeAdapter: Any): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, typeAdapter)


public data class SerializerArg<T> (
        val src: T,
        val type: Type,
        val context: SerializerArg.Context
) {
    public class Context(public val gsonContext: JsonSerializationContext) {
        public inline fun <reified T: Any> serialize(src: T) = gsonContext.serialize(src, typeToken<T>())
        public fun <T: Any> serialize(src: T, type: Type) = gsonContext.serialize(src, type)
    }
}

public data class DeserializerArg (
        val json: JsonElement,
        val type: Type,
        val context: DeserializerArg.Context
) {
    public class Context(public val gsonContext: JsonDeserializationContext) {
        public inline fun <reified T: Any> deserialize(json: JsonElement) = gsonContext.deserialize<T>(json, typeToken<T>())
        public fun <T: Any> deserialize(json: JsonElement, type: Type) = gsonContext.deserialize<T>(json, type)
    }
}

public abstract class RegistrationBuilder<T: Any> {
    fun serialize(serializer: (arg: SerializerArg<T>) -> JsonElement) = typeAdapter(jsonSerializer(serializer))
    fun deserialize(deserializer: (DeserializerArg) -> T?) = typeAdapter(jsonDeserializer(deserializer))
    fun createInstances(creator: (type: Type) -> T) = typeAdapter(instanceCreator(creator))

    abstract fun typeAdapter(typeAdapter: Any)
}

public class SimpleRegistrationBuilder<T: Any>(val builder: GsonBuilder, val type: Type) : RegistrationBuilder<T>() {

    override fun typeAdapter(typeAdapter: Any) {
        builder.registerTypeAdapter(type, typeAdapter)
    }
}

public inline fun <reified T: Any> GsonBuilder.registerTypeAdapter(noinline init: RegistrationBuilder<T>.() -> Unit): GsonBuilder {
    SimpleRegistrationBuilder<T>(this, typeToken<T>()).init()
    return this
}

public class HierarchyRegistrationBuilder<T: Any>(val builder: GsonBuilder, val type: Class<T>) : RegistrationBuilder<T>() {
    override fun typeAdapter(typeAdapter: Any) {
        builder.registerTypeHierarchyAdapter(type, typeAdapter)
    }
}

public inline fun <reified T: Any> GsonBuilder.registerTypeHierarchyAdapter(noinline init: RegistrationBuilder<T>.() -> Unit): GsonBuilder {
    HierarchyRegistrationBuilder(this, T::class.java).init()
    return this
}
