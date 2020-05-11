package com.github.salomonbrys.kotson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RWRegistrationSpecs : Spek({

    given("a non-generic stream type adapter") {

        val adapter = typeAdapter<Person> {
            write { beginArray().value(it.name).value(it.age).endArray() }
            read { beginArray() ; val p = Person(nextString(), nextInt()) ; endArray() ; p }
        }

        val gson = GsonBuilder()
                .registerTypeAdapter<Person>(adapter)
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

    given("a specialized stream type adapter") {

        val gson = GsonBuilder()
                .registerTypeAdapter<GenericPerson<Int>> {
                    write { beginArray().value(it.name).value(it.info).endArray() }
                    read { beginArray() ; val p = GenericPerson(nextString(), nextInt()) ; endArray() ; p }
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

            it("should not deserialize differently parameterized type accordingly") {
                assertFailsWith<JsonSyntaxException> { gson.fromJson<GenericPerson<String>>("[\"Salomon\", \"Brys\"]") }
            }
        }
    }

    given ("a bad type adapter") {
        on("definition of both serialize and read") {
            it("should throw an exception") {
                assertFailsWith<IllegalArgumentException> {
                    GsonBuilder()
                            .registerTypeAdapter<Person> {
                                serialize { jsonArray(it.src.name, it.src.age) }
                                read { beginArray() ; val p = Person(nextString(), nextInt()) ; endArray() ; p }
                            }
                            .create()
                }
            }
        }

        on("definition of only write") {
            it("should throw an exception") {
                assertFailsWith<IllegalArgumentException> {
                    GsonBuilder()
                            .registerTypeAdapter<Person> {
                                write { beginArray().value(it.name).value(it.age).endArray() }
                            }
                            .create()
                }
            }
        }
    }

})
