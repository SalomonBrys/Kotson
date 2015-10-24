
[![Kotlin 1.0.0-beta-1038](https://img.shields.io/badge/Kotlin-1.0.0--beta--1038-blue.svg)](http://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.salomonbrys.kotson/kotson.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.salomonbrys.kotson%22)
[![Travis](https://img.shields.io/travis/SalomonBrys/Kotson.svg)](https://travis-ci.org/SalomonBrys/Kotson/builds)
[![MIT License](https://img.shields.io/github/license/SalomonBrys/Kotson.svg)](https://github.com/SalomonBrys/Kotson/blob/master/LICENSE.txt)
[![GitHub issues](https://img.shields.io/github/issues/SalomonBrys/Kotson.svg)](https://github.com/SalomonBrys/Kotson/issues)
[![Slack channel](https://img.shields.io/badge/Chat-Slack-green.svg)](https://kotlinlang.slack.com/messages/kotson/)


Kotson: *Gson* for *Kotlin*
===========================

Kotson enables you to parse and write JSON with [Google's Gson](https://github.com/google-gson/google-gson) using a conciser and easier syntax.

Kotson is a set of *extension functions*, meaning that it adds utility functions and syntactic sugars to Gson in Kotlin. It does not add new features to Gson nor does it creates new types. It is therefore usable on any Gson object, whether created from java or kotlin in source or library.


Install
-------

Maven:

    <dependency>
    	<groupId>com.github.salomonbrys.kotson</groupId>
    	<artifactId>kotson</artifactId>
    	<version>1.5.1</version>
    </dependency>

Gradle:

    compile 'com.github.salomonbrys.kotson:kotson:1.5.1'

 - version 1.1.0 is compatible with Kotlin M11
 - version 1.2.0 is compatible with Kotlin M12
 - version 1.3.1 is compatible with Kotlin M13
 - version 1.4.1 is compatible with Kotlin M14
 - version 1.5.1 is compatible with Kotlin 1.0.0-beta-1038


Creating Json
-------------

JsonObject:

```kotlin
val obj: JsonObject = jsonObject(
    "name" to "kotson",
    "creation" to Date().getTime(),
    "files" to 4
)
```

JsonArray:

```kotlin
val arr: JsonArray = jsonArray("one", "two", 42, 21.5)
```

Of course, a combination of both:

```kotlin
val obj: JsonObject = jsonObject(
    "property" to "value",
    "array" to jsonArray(
        21,
        "fourty-two",
        jsonObject("go" to "hell")
    )
)
```

JsonPrimitives:

```kotlin
42.toJson()         // java: new JsonPrimitive(42);
42.21f.toJson()     // java: new JsonPrimitive(42.21f);
42.21.toJson()      // java: new JsonPrimitive(42.21d);
"coucou".toJson()   // java: new JsonPrimitive("coucou");
'c'.toJson()        // java: new JsonPrimitive('c');
true.toJson()       // java: new JsonPrimitive(true);
```


Setting custom (de)serializers
------------------------------

If you need to register a `serializer` / `deserializer` / `InstanceCreator`, you can use one of those APIs:

```kotlin
gsonBuilder.serialize<MyType> { src, type, context -> /* return a JsonElement */ }
gsonBuilder.serializeHierarchy<MyType> { src, type, context -> /* return a JsonElement */ }
gsonBuilder.simpleSerialize<MyType> { src -> /* return a JsonElement */ }
gsonBuilder.simpleSerializeHierarchy<MyType> { src -> /* return a JsonElement */ }

gsonBuilder.deserialize<MyType> { json, type, context -> /* return a MyType */ }
gsonBuilder.deserializeHierarchy<MyType> { json, type, context -> /* return a MyType */ }
gsonBuilder.simpleDeserialize<MyType> { json -> /* return a MyType */ }
gsonBuilder.simpleDeserializeHierarchy<MyType> { json -> /* return a MyType */ }

gsonBuilder.createInstances<MyType> { type -> /* return a new MyType */ }
gsonBuilder.createHierarchyInstances<MyType> { type -> /* return a new MyType */ }
```


Parsing JSON
------------

Kotson provides a simple API that does not suffer from Java's type erasure. That means that whatever the output type, it will be parsed correctly and eliminates the need for `TypeToken`.

```kotlin
// java: List<User> list = gson.fromJson(src, new TypeToken<List<User>>(){}.getType());
val list = gson.fromJson<List<User>>(jsonString)
val list = gson.fromJson<List<User>>(jsonElement)
val list = gson.fromJson<List<User>>(jsonReader)
val list = gson.fromJson<List<User>>(reader)
```

A lot of Gson's APIs are relying on `java.lang.reflect.Type` to specify a type, but Kotlin's `javaClass` returns a `java.lang.Class` which is a Type but suffers from type erasure. To mediate this issue, Gson uses `TypeToken` to create `java.lang.reflect.Type` objects without type erasure.
If you need such a Type object, you can simply use the `typeToken` function the same way you use the `javaClass` function. For example: `typeToken<Map<String, List<User>>>()`


Browsing Json Elements
----------------------

Kotson allows you to simply convert a jsonElement to a primitive, a `JsonObject` or a `JsonArray`:
```kotlin
val s = json.string // java: String s = json.getAsString();
val i = json.int    // java: int i = json.getAsBoolean();
val a = json.array  // java: JsonArray = json.getAsJsonArray();
val o = json.obj    // java: JsonObject = json.getAsJsonObject();

val s = json.nullString // java: String s = json.isJsonNull() ? null : json.getAsString();
val i = json.nullInt    // java: Integer i = json.isJsonNull() ? null : json.getAsInt();
val a = json.nullArray  // java: JsonArray = json.isJsonNull() ? null : json.getAsJsonArray();
val o = json.nullObj    // java: JsonObject = json.isJsonNull() ? null : json.getAsJsonObject();
```

The same APIs exist for `.string`, `.bool`, `.byte`, `.char`, `.short`, `.int`, `.long`, `.float`, `.double`, `.number`, `.bigInteger`, `.bigDecimal`, `.array`, `.obj`

Kotson provides a simple API that allows you to easily browse `JsonElement`, `JsonObject` and `JsonArray`:


```kotlin
// java: JsonElement components = colors.getAsJsonObject().get("orange");
val components = colors["orange"]

// java: JsonElement greenComp = components.getAsJsonArray().get(1);
val greenComp = components[1]


// java: int greenComp = json .getAsJsonObject()
//                            .getAsJsonObject("colors")
//                            .getAsJsonArray("orange")
//                            .get(1)
//                            .getAsInt();

val greenComp = json["colors"]["orange"][1].int
```


Mutating Json Elements
----------------------

Kotson allows you to mutate a `JsonObject` or a `JsonArray`:

```kotlin
val array = jsonArray("zero", "x", "two")
array[1] = "one"
array += "three"
array -= "zero"

val obj = jsonObject()
obj["this"] = "that"
obj += "answer" to 42
obj -= "this"
```


Copying Json Elements
---------------------

Kotson allows you to make a shallow copy (single-level copy) or a deep copy (recursive copy) of a `JsonObject` or a `JsonArray`:

```kotlin
val shallow = json.shallowCopy()
val deep = json.deepCopy()
```


Accessing object fields via property delegates
----------------------------------------------

Kotson allows you to delegate properties to `JsonObject` fields:

```kotlin
class Person(public val obj: JsonObject) {
    val name: String by obj.byString
    val age: Int by obj.byInt
}
```
