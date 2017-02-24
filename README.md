[![Kotlin 1.0.6](https://img.shields.io/badge/Kotlin-1.0.6-blue.svg)](http://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.salomonbrys.kotson/kotson.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.salomonbrys.kotson%22)
[![Travis](https://img.shields.io/travis/SalomonBrys/Kotson.svg)](https://travis-ci.org/SalomonBrys/Kotson/builds)
[![MIT License](https://img.shields.io/github/license/SalomonBrys/Kotson.svg)](https://github.com/SalomonBrys/Kotson/blob/master/LICENSE.txt)
[![GitHub issues](https://img.shields.io/github/issues/SalomonBrys/Kotson.svg)](https://github.com/SalomonBrys/Kotson/issues)
[![Slack channel](https://img.shields.io/badge/Chat-Slack-green.svg)](https://kotlinlang.slack.com/messages/kotson/)
[![Donate](https://img.shields.io/badge/Backing-Donate-orange.svg)](https://donorbox.org/donation-salomonbrys/)



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
    	<version>2.5.0</version>
    </dependency>

Gradle:

    compile 'com.github.salomonbrys.kotson:kotson:2.5.0'


Table Of Contents
-----------------

  * [Kotson: <em>Gson</em> for <em>Kotlin</em> ](#kotson-gson-for-kotlin)
    * [Install](#install)
    * [Table Of Contents](#table-of-contents)
  * [Usage](#usage)
    * [Creating Json](#creating-json)
    * [Setting custom (de)serializers](#setting-custom-deserializers)
    * [Setting custom Readers and Writers](#setting-custom-readers-and-writers)
    * [Parsing JSON](#parsing-json)
    * [Browsing Json Elements](#browsing-json-elements)
    * [Mutating Json Elements](#mutating-json-elements)
    * [Copying Json Elements](#copying-json-elements)
    * [Accessing object fields via property delegates](#accessing-object-fields-via-property-delegates)
  * [Let's talk!](#lets-talk)
  * [Donate](#donate)



Usage
=====


Creating Json
-------------

JsonObject:

```kotlin
import com.github.salomonbrys.kotson.*

val obj: JsonObject = jsonObject(
    "name" to "kotson",
    "creation" to Date().getTime(),
    "files" to 4
)
```

JsonArray:

```kotlin
import com.github.salomonbrys.kotson.*

val arr: JsonArray = jsonArray("one", "two", 42, 21.5)
```

Of course, a combination of both:

```kotlin
import com.github.salomonbrys.kotson.*

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
import com.github.salomonbrys.kotson.*

val pi = 42.toJson()         // java: new JsonPrimitive(42);
val pf = 42.21f.toJson()     // java: new JsonPrimitive(42.21f);
val pd = 42.21.toJson()      // java: new JsonPrimitive(42.21d);
val pc = 'c'.toJson()        // java: new JsonPrimitive('c');
val pz = true.toJson()       // java: new JsonPrimitive(true);
val os = "coucou".toJson()   // java: new JsonPrimitive("coucou");
```


Setting custom (de)serializers
------------------------------

If you need to register a `serializer` / `deserializer` / `InstanceCreator`, you can use these "builder" APIs:

```kotlin
import com.github.salomonbrys.kotson.*

val gson = GsonBuilder()
    .registerTypeAdapter<MyType> {
    
        serialize {
            /*
                it.type: Type to serialize from
                it.context: GSon context
                it.src : Object to serialize
            */
        }
        
        deserialize {
            /*
                it.type: Type to deserialize to
                it.context: GSon context
                it.json : JsonElement to deserialize from
            */
        }
        
        createInstances {
            /*
                it: Type of instance to create
            */
        }
    
    }
    .registerTypeHierarchyAdapter<MyOtherType> {
        serialize { /* Same a above */ }
        deserialize { /* Same a above */ }
        createInstances { /* Same a above */ }
    }
    .create()
```

Of course, you can declare only a serializer, a deserializer or an instance creator. You don't *have* to declare all three.

You don't have to declare all your `JsonSerializer`s, `JsonDeserializer`s and `InstanceCreator`s where you initialize Gson.
Kotson allows you to create those type adapters in different files using the functions `jsonSerializer`, `jsonDeserializer` and `instanceCreator`.
You will then be able to register those when creating the Gson object:

```kotlin
//TypeAdapters.kt
import com.github.salomonbrys.kotson.*

val personSerializer = jsonSerializer { /* Same arguments as before */ }
```
```kotlin
//Main.kt
import com.github.salomonbrys.kotson.*

val gson = GsonBuilder().registerTypeAdapter<Person>(personSerializer).create()
```


Setting custom Readers and Writers
----------------------------------

Gson has another API (named Stream API) that allows to register writers (to `JsonWriter`) and readers (from `JsonReader`).  
Here is an example for a simple `Person` class:

```kotlin
import com.github.salomonbrys.kotson.*

val gson = GsonBuilder()
    .registerTypeAdapter<Person> {
    
        write {
            beginArray()
            value(it.name)
            value(it.age)
            endArray()
        }
        
        read {
            beginArray()
            val name = nextString()
            val age = nextInt()
            endArray()
    
            Person(name, age)
        }
    
    }
    .create()
```

While a bit more complex and difficult to handle, this API is also better optimized. So if you're after performance, I recommend you use this API.

Using this API has a few drawbacks:

 * You must define both `write` and `read`
 * You cannot use a mix (`serialize` + `read` or `write` + `deserialize`).
 * In `read`, you cannot access the exact object type that you are deserializing to (the `it.type` of deserialize).

If you wish to register a nullable reader or writer, you can use `registerNullableTypeAdapter` instead.

You don't have to declare all your `TypeAdapter`s where you initialize Gson.
Kotson allows you to create those type adapters in different files using the functions `typeAdapter` and `nullableTypeAdapter`.
You will then be able to register those when creating the Gson object:

```kotlin
//TypeAdapters.kt
import com.github.salomonbrys.kotson.*

val personTypeAdapter = typeAdapter<Person> {
    write { /*...*/ }
    read { /*...*/ }
}
```
```kotlin
//Main.kt
import com.github.salomonbrys.kotson.*

val gson = GsonBuilder().registerTypeAdapter<Person>(personSerializer).create()
```

Kotson provides no utility function for the `TypeAdapterFactory` interface.
Because this interface defines a generic function, there is currently no other way to use it other than implementing it on a regular `object` or `class`.


Parsing JSON
------------

Kotson provides a simple API that does not suffer from Java's type erasure. That means that whatever the output type, it will be parsed correctly and eliminates the need for `TypeToken`.

```kotlin
import com.github.salomonbrys.kotson.*

val gson = Gson()

// java: List<User> list = gson.fromJson(src, new TypeToken<List<User>>(){}.getType());
val list1 = gson.fromJson<List<User>>(jsonString)
val list2 = gson.fromJson<List<User>>(jsonElement)
val list3 = gson.fromJson<List<User>>(jsonReader)
val list4 = gson.fromJson<List<User>>(reader)
```

Attention: `gson.fromJson<MyType>` will return a non-nullable type whereas `gson.fromJson<MyType?>` will return a nullable type. Therefore the code `gson.fromJson<MyType>("null")` is correct and will throw a null-pointer exception!

A lot of Gson's APIs are relying on `java.lang.reflect.Type` to specify a type, but Kotlin's `javaClass` returns a `java.lang.Class` which is a Type but suffers from type erasure. To mediate this issue, Gson uses `TypeToken` to create `java.lang.reflect.Type` objects without type erasure.
If you need such a Type object, you can simply use the `typeToken` function the same way you use the `javaClass` function. For example: `typeToken<Map<String, List<User>>>()`

Careful: the `typeToken` function behaves slightly differently then Gson's `TypeToken` class.
When providing a non-specialized generic type, `typeToken<List<*>>` will return `Class<List>` (while Gson's mechanism will return a `ParameterizedType`).
If you really need a `ParameterizedType` for a non-specialized generic type, you can use the `gsonTypeToken` function.


Browsing Json Elements
----------------------

Kotson allows you to simply convert a jsonElement to a primitive, a `JsonObject` or a `JsonArray`:
```kotlin
import com.github.salomonbrys.kotson.*

val s = json.string // java: String s = json.getAsString();
val i = json.int    // java: int i = json.getAsInt();
val a = json.array  // java: JsonArray = json.getAsJsonArray();
val o = json.obj    // java: JsonObject = json.getAsJsonObject();

val ns = json.nullString // java: String s = json.isJsonNull() ? null : json.getAsString();
val ni = json.nullInt    // java: Integer i = json.isJsonNull() ? null : json.getAsInt();
val na = json.nullArray  // java: JsonArray = json.isJsonNull() ? null : json.getAsJsonArray();
val no = json.nullObj    // java: JsonObject = json.isJsonNull() ? null : json.getAsJsonObject();
```

The same APIs exist for `.string`, `.bool`, `.byte`, `.char`, `.short`, `.int`, `.long`, `.float`, `.double`, `.number`, `.bigInteger`, `.bigDecimal`, `.array`, `.obj`

Kotson provides a simple API that allows you to easily browse `JsonElement`, `JsonObject` and `JsonArray`:


```kotlin
import com.github.salomonbrys.kotson.*

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
import com.github.salomonbrys.kotson.*

fun test() {
    val array = jsonArray("zero", "x", "two")
    array[1] = "one"
    array += "three"
    array -= "zero"

    val obj = jsonObject()
    obj["this"] = "that"
    obj += "answer" to 42
    obj -= "this"
}
```


Copying Json Elements
---------------------

Kotson allows you to make a shallow copy (single-level copy) or a deep copy (recursive copy) of a `JsonObject` or a `JsonArray`:

```kotlin
import com.github.salomonbrys.kotson.*

val shallow = json.shallowCopy()
val deep = json.deepCopy()
```


Accessing object fields via property delegates
----------------------------------------------

Kotson allows you to delegate properties to `JsonObject` fields:

```kotlin
import com.github.salomonbrys.kotson.*

class Person(public val obj: JsonObject) {
    val id: String by obj.byString               // Maps to obj["id"]
    val name: String by obj.byString("fullName") // Maps to obj["fullName"]
    val birthDate: Int by obj["dates"].byInt(0)  // Maps to obj["dates"][0]
}
```



Let's talk!
===========

You've read so far ?! You're awsome!
Why don't you drop by the [Kotson Slack channel](https://kotlinlang.slack.com/messages/kotson/) on Kotlin's Slack group?



Donate
======

Kotson is free to use for both non-profit and commercial use and always will be.

If you wish to show some support or appreciation to my work, you are free to **[donate](https://donorbox.org/donation-salomonbrys)**!

This would be (of course) greatly appreciated but is by no means necessary to receive help or support, which I'll be happy to provide for free :)
