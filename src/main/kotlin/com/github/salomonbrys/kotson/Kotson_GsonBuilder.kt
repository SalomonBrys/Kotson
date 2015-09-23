package com.github.salomonbrys.kotson

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type


public inline fun <reified T: Any> typeToken(): Type
        = object : TypeToken<T>() {} .type



public fun <T: Any> typeAdapter(fRead: (JsonReader) -> T, fWrite: (JsonWriter, T) -> Unit): TypeAdapter<T>
    = object: TypeAdapter<T>() {
        override fun read(reader: JsonReader): T = fRead(reader)
        override fun write(writer: JsonWriter, value: T) = fWrite(writer, value)
    }

public fun <T: Any> jsonSerializer(serializer: (src: T, type: Type, context: JsonSerializationContext) -> JsonElement): JsonSerializer<T>
        = JsonSerializer { src, type, context -> serializer(src, type, context) }

public fun <T: Any> simpleJsonSerializer(serializer: (src: T) -> JsonElement): JsonSerializer<T>
        = JsonSerializer { src, type, context -> serializer(src) }

public fun <T: Any> jsonDeserializer(deserializer: (json: JsonElement, type: Type, context: JsonDeserializationContext) -> T?): JsonDeserializer<T>
        = JsonDeserializer<T> { json, type, context -> deserializer(json, type, context) }

public fun <T: Any> simpleJsonDeserializer(deserializer: (json: JsonElement) -> T?): JsonDeserializer<T>
        = JsonDeserializer<T> { json, type, context -> deserializer(json) }

public fun <T: Any> instanceCreator(creator: (type: Type) -> T): InstanceCreator<T>
        = InstanceCreator { creator(it) }



public inline fun <reified T: Any> GsonBuilder.registerTypeAdapter(typeAdapter: Any): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), typeAdapter)

public inline fun <reified T: Any> GsonBuilder.registerTypeHierarchyAdapter(typeAdapter: Any): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, typeAdapter)



public inline fun <reified T: Any> GsonBuilder.adapt(noinline fRead: (JsonReader) -> T, noinline fWrite: (JsonWriter, T) -> Unit): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), typeAdapter(fRead, fWrite))

public inline fun <reified T: Any> GsonBuilder.adaptHierarchy(noinline fRead: (JsonReader) -> T, noinline fWrite: (JsonWriter, T) -> Unit): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, typeAdapter(fRead, fWrite))



public inline fun <reified T: Any> GsonBuilder.serialize(noinline serializer: (src: T, type: Type, context: JsonSerializationContext) -> JsonElement): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), jsonSerializer(serializer))

public inline fun <reified T: Any> GsonBuilder.serializeHierarchy(noinline serializer: (src: T, type: Type, context: JsonSerializationContext) -> JsonElement): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, jsonSerializer(serializer))

public inline fun <reified T: Any> GsonBuilder.simpleSerialize(noinline serializer: (src: T) -> JsonElement): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), simpleJsonSerializer(serializer))

public inline fun <reified T: Any> GsonBuilder.simpleSerializeHierarchy(noinline serializer: (src: T) -> JsonElement): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, simpleJsonSerializer(serializer))



public inline fun <reified T: Any> GsonBuilder.deserialize(noinline deserializer: (json: JsonElement, type: Type, context: JsonDeserializationContext) -> T?): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), jsonDeserializer(deserializer))

public inline fun <reified T: Any> GsonBuilder.deserializeHierarchy(noinline deserializer: (json: JsonElement, type: Type, context: JsonDeserializationContext) -> T?): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, jsonDeserializer(deserializer))

public inline fun <reified T: Any> GsonBuilder.simpleDeserialize(noinline deserializer: (json: JsonElement) -> T?): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), simpleJsonDeserializer(deserializer))

public inline fun <reified T: Any> GsonBuilder.simpleDeserializeHierarchy(noinline deserializer: (json: JsonElement) -> T?): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, simpleJsonDeserializer(deserializer))



public inline fun <reified T: Any> GsonBuilder.createInstances(noinline creator: (type: Type) -> T): GsonBuilder
        = this.registerTypeAdapter(typeToken<T>(), instanceCreator(creator))

public inline fun <reified T: Any> GsonBuilder.createHierarchyInstances(noinline creator: (type: Type) -> T): GsonBuilder
        = this.registerTypeHierarchyAdapter(T::class.java, instanceCreator(creator))
