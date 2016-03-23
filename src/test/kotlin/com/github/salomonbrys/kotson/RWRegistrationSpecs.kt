package com.github.salomonbrys.kotson

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import org.jetbrains.spek.api.shouldBeTrue
import org.jetbrains.spek.api.shouldEqual

class RWRegistrationSpecs : Spek({

    given("a non-generic stream type adapter") {

        val gson = GsonBuilder()
                .registerTypeAdapter<Person> {
                    write { beginArray().value(it.name).value(it.age).endArray() }
                    read { beginArray() ; val p = Person(nextString(), nextInt()) ; endArray() ; p }
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

    given("a specialized stream type adapter") {

        val gson = GsonBuilder()
                .registerTypeAdapter<GenericPerson<Int>> {
                    write { beginArray().value(it.name).value(it.info).endArray() }
                    read { beginArray() ; val p = GenericPerson(nextString(), nextInt()) ; endArray() ; p }
                }
                .create()

        on("serializattion") {

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

    given ("a bad type adapter") {
        on("definition of both serialize and read") {
            it("should throw an exception") {
                shouldThrow<IllegalArgumentException> {
                    val gson = GsonBuilder()
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
                shouldThrow<IllegalArgumentException> {
                    val gson = GsonBuilder()
                            .registerTypeAdapter<Person> {
                                write { beginArray().value(it.name).value(it.age).endArray() }
                            }
                            .create()
                }
            }
        }
    }

})
