package com.github.salomonbrys.kotson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.lang.reflect.ParameterizedType
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SDRegistrationSpecs : Spek({

    given("a non-generic type adapter") {
        val gson = GsonBuilder()
                .registerTypeAdapter<Person> {
                    serialize { jsonArray(it.src.name, it.src.age) }
                    deserialize { Person(it.json[0].string, it.json[1].int) }
                }
                .create()

        on("serialization") {

            it("should serialize accordingly") {
                val json = gson.toJsonTree(Person("Salomon", 29))

                assertTrue(json is JsonArray)
                assertEquals("Salomon", json[0].string)
                assertEquals(29, json[1].int)
            }
        }

        on("deserialization") {

            it("should deserialize accordingly") {
                val person = gson.fromJson<Person>("[\"Salomon\", 29]")

                assertEquals(Person("Salomon", 29), person)
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

        on("serializattion") {

            it("should serialize specific type accordingly") {
                val json = gson.typedToJsonTree(GenericPerson("Salomon", 29))
                assertTrue(json is JsonArray)
                assertEquals("Salomon", json[0].string)
                assertEquals(29, json[1].int)
            }

            it("should not serialize differently parameterized type accordingly") {
                val json = gson.typedToJsonTree(GenericPerson("Salomon", "Brys"))
                assertTrue(json is JsonObject)
            }

        }

        on("deserialization") {

            it("should deserialize specific type accordingly") {
                val person = gson.fromJson<GenericPerson<Int>>("[\"Salomon\", 29]")

                assertEquals(GenericPerson("Salomon", 29), person)
            }

            it("Should not deserialize differently parameterized type accordingly") {
                assertFailsWith<JsonSyntaxException> { gson.fromJson<GenericPerson<String>>("[\"Salomon\", \"Brys\"]") }
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

            it("should serialize any parameterized type accordingly (1)") {
                val json = gson.typedToJsonTree(GenericPerson("Salomon", 29))
                assertTrue(json is JsonArray)
                assertEquals("Salomon", json[0].string)
                assertEquals(29, json[1].int)
            }

            it("should serialize any parameterized type accordingly (2)") {
                val json = gson.typedToJsonTree(GenericPerson("Salomon", "Brys"))
                assertTrue(json is JsonArray)
                assertEquals("Salomon", json[0].string)
                assertEquals("Brys", json[1].string)
            }
        }

        on("deserialization") {

            it("should deserialize any parameterized type accordingly (1)") {
                val person = gson.fromJson<GenericPerson<Int>>("[\"Salomon\", 29]")

                assertEquals(GenericPerson("Salomon", 29), person)
            }

            it("should deserialize any parameterized type accordingly (2)") {
                val person = gson.fromJson<GenericPerson<String>>("[\"Salomon\", \"Brys\"]")

                assertEquals(GenericPerson("Salomon", "Brys"), person)
            }
        }
    }

})
