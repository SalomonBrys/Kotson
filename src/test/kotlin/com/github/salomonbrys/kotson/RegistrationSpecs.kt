package com.github.salomonbrys.kotson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import org.jetbrains.spek.api.shouldBeTrue
import org.jetbrains.spek.api.shouldEqual
import java.lang.reflect.ParameterizedType

class RegistrationSpecs : Spek({

    given("a non-generic type adapter") {

        val gson = GsonBuilder()
            .registerTypeAdapter<Person> {
                serialize { jsonArray(it.src.name, it.src.age) }
                deserialize { Person(it.json[0].string, it.json[1].int) }
            }
            .create()

        on("serialization") {

            it("Should serialize accordingly") {
                val json = gson.toJsonTree(Person("Salomon", 29))

                shouldBeTrue(json is JsonArray)
                shouldEqual("Salomon", json[0].string)
                shouldEqual(29, json[1].int)
            }
        }

        on("deserialization") {

            it("Should deserialize accordingly") {
                val person = gson.fromJson<Person>("[\"Salomon\", 29]")

                shouldEqual(Person("Salomon", 29), person)
            }
        }

    }

    given("a specialized type adapter") {
        val gson = GsonBuilder()
                .registerTypeAdapter<GenericPerson<Int>> {
                    serialize { jsonArray(it.src.name, it.src.info) }
                    deserialize { GenericPerson(it.json[0].string, it.json[1].int) }
                }
                .create()

        on("serialization") {

            it("Should serialize specific type accordingly") {
                val json = gson.typedToJsonTree(GenericPerson("Salomon", 29))
                shouldBeTrue(json is JsonArray)
                shouldEqual("Salomon", json[0].string)
                shouldEqual(29, json[1].int)
            }

            it("Should not serialize differently parameterized type accordingly") {
                val json = gson.typedToJsonTree(GenericPerson("Salomon", "Brys"))
                shouldBeTrue(json is JsonObject)
            }

        }

        on("deserialization") {

            it("Should deserialize specific type accordingly") {
                val person = gson.fromJson<GenericPerson<Int>>("[\"Salomon\", 29]")

                shouldEqual(GenericPerson("Salomon", 29), person)
            }

            it("Should not deserialize differently parameterized type accordingly") {
                shouldThrow<JsonSyntaxException> { gson.fromJson<GenericPerson<String>>("[\"Salomon\", \"Brys\"]") }
            }
        }

    }

    given("a generic type adapter") {
        val gson = GsonBuilder()
                .registerTypeAdapter<GenericPerson<*>> {
                    serialize { jsonArray(it.src.name, it.src.info) }
                    deserialize {
                        val infoType = (it.type as ParameterizedType).actualTypeArguments[0]
                        GenericPerson(it.json[0].string, it.context.deserialize<Any>(it.json[1], infoType))
                    }
                }
                .create()

        on("serialization") {

            it("Should serialize any parameterized type accordingly (1)") {
                val json = gson.typedToJsonTree(GenericPerson("Salomon", 29))
                shouldBeTrue(json is JsonArray)
                shouldEqual("Salomon", json[0].string)
                shouldEqual(29, json[1].int)
            }

            it("Should serialize any parameterized type accordingly (2)") {
                val json = gson.typedToJsonTree(GenericPerson("Salomon", "Brys"))
                shouldBeTrue(json is JsonArray)
                shouldEqual("Salomon", json[0].string)
                shouldEqual("Brys", json[1].string)
            }
        }

        on("deserialization") {

            it("Should deserialize any parameterized type accordingly (1)") {
                val person = gson.fromJson<GenericPerson<Int>>("[\"Salomon\", 29]")

                shouldEqual(GenericPerson("Salomon", 29), person)
            }

            it("Should deserialize any parameterized type accordingly (2)") {
                val person = gson.fromJson<GenericPerson<String>>("[\"Salomon\", \"Brys\"]")

                shouldEqual(GenericPerson("Salomon", "Brys"), person)
            }

        }

    }

})
