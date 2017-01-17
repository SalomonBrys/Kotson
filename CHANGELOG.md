
#### 2.5.0 (17-01-2017)

 * `typeToken` removes generic type parameter wildcards. This means that `typeToken<List<Something>>` will now return `List<Something>` instead of `List<? extends Something>`.
 * Added the `byNullable*` functions to delegate to nullable values inside a `JsonObject` or `JsonArray` (thanks to @eygraber Eliezer Graber).
 * Added the possibility to define a default value to the delegate `by*` methods (thanks to @eygraber Eliezer Graber).
 * Added the `addProperty` and `addPropertyIfNotNull` methods to `JsonObject` (thanks to @ColinHebert Colin Hebert).
 * Added the ability to add a `JsonSerializer` and `JsonDeserializer` with `registerTypeAdapter` (thanks to @ColinHebert Colin Hebert).
 * Gson `2.8.0`, see [changelog](https://github.com/google/gson/blob/master/CHANGELOG.md#version-28).
 * Kotlin `1.0.6`
 * Gradle `3.3`


#### 2.4.0 (10-08-2016)

 * `typeToken` is more permissive with generic parameters. It tries to infer generic wildcards.
 * Kotlin `1.0.3`
 * Gradle `2.14.1`.
 * Spek `1.0.25`.


#### 2.3.0 (22-06-2016)

 * In a `JsonSerializer`, using `it.context.serialize` now serializes according to the object real type (at runtime), which is consistent with `Gson.toJson`.
   Serializing according to the compile time type is possible with `it.context.typedSerialize`, which is consistent with `Gson.typedToJson`.
 * Gson `2.7`, see [changelog](https://github.com/google/gson/blob/master/CHANGELOG.md#version-27).
 * Kotlin `1.0.2-1`

#### 2.2.2 (13-06-2016)

 * `jsonArray` and `jsonObject` functions are now usable with `Iterable` and `Sequence`.
 * Gradle `2.13`.

#### 2.2.1 (23-05-2016)

 * Kotlin `1.0.2`.
 * Spek `1.0.9`.

#### 2.2.0 (30-03-2016)

 * Kotlin `1.0.1`.
 * Gson `2.6.2`.
 * Spek `1.0.0`.
 * Added `read` and `write` to `registerTypeAdapter` and `registerTypeHierarchyAdapter`.
 * Added `registerNullableTypeAdapter` abd `registerNullableTypeHierarchyAdapter`.
 * Renamed `typeToken` to `gsonTypeToken` and `registrationTypeToken` to `typeToken`. That means that `typeToken<List<*>>` will now return a `Class`.
   If still want a `ParametrizedType` corresponding to java's `List<? extends Object>`, you should use `gsonTypeToken`.

#### 2.1.0 (16-02-2016)
 * Kotlin `1.0.0`.
 * gson `2.6.1`.

#### 2.0.1 (08-02-2016)
 * Correcting #11: `typeToken` is back to its classic gson semantic.
 * `registrationTypeToken` has the semantic of the previous `typeToken`.

#### 2.0.0 (05-02-2016)
 * Kotlin `1.0.0-beta-1036`.
 * Gson `2.5`.
 * Introducing `typedToJson`.
 * `typeToken` now returns a `Class` instead of a `ParameterizedType` when for non specialized generic types (such as `List<*>*`).
 * New syntax for `registerTypeAdapter` and `registerTypeHierarchyAdapter` for declaring type adapters (serializers and deserializers).
 