package com.github.salomonbrys.kotson

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BuildSpecs : Spek({

    given("a JsonObject with only primitives") {

        val obj = jsonObject(
                "number" to 42,
                "char" to 'c',
                "bool" to true,
                "string" to "Hello, world!",
                "null" to null
        )

        on("creation") {

            it("should contain corresponding JSonPrimitives") {

                assertEquals(42.toJson(), obj["number"])
                assertEquals('c'.toJson(), obj["char"])
                assertEquals(true.toJson(), obj["bool"])
                assertEquals("Hello, world!".toJson(), obj["string"])
                assertEquals(jsonNull, obj["null"])

            }

            it("should have only the declared values") {
                assertEquals(5, obj.size())
            }
        }

        on("queried for an inexisting key") {

            it("should throw a NoSuchElementException when queried with getNotNull") {
                assertFailsWith<NoSuchElementException> { obj.getNotNull("nothing") }
            }

            it("should return null when queried with get") {

                assertNull(obj["nothing"])

            }

        }

        on("queried for an index") {

            it("should throw an IllegalStateException") {
                assertFailsWith<IllegalStateException> { obj[42] }
            }

        }
    }

    given("a JsonArray with only primitives") {

        val array = jsonArray(
                42,
                'c',
                true,
                "Hello, world!",
                null
        )

        on("creation") {

            it("should contain corresponding JSonPrimitives") {

                assertEquals(42.toJson(), array[0])
                assertEquals('c'.toJson(), array[1])
                assertEquals(true.toJson(), array[2])
                assertEquals("Hello, world!".toJson(), array[3])
                assertEquals(jsonNull, array[4])

            }

            it("should have only the declared values") {
                assertEquals(5, array.size())
            }
        }

        on("queried for an inexisting index") {

            it("should throw a IndexOutOfBoundsException") {
                assertFailsWith<IndexOutOfBoundsException> { array[5] }
            }

        }

        on("queried for a key") {

            it("should throw an IllegalStateException") {
                assertFailsWith<IllegalStateException> { array["nothing"] }
            }

        }

    }

    given("a complex entanglement of JsonObject and JsonArray") {

        val obj = jsonObject(
                "number" to 42,
                "a1" to jsonArray(
                        'c',
                        jsonObject(
                                "bool" to true,
                                "a2" to jsonArray(
                                        "Hello, world!",
                                        jsonObject(
                                                "null" to null
                                        )
                                )
                        )
                )
        )

        on("access") {

            it ("should give the correct values") {

                assertEquals(42, obj["number"].int)
                assertEquals('c', obj["a1"][0].char)
                assertTrue(obj["a1"][1]["bool"].bool)
                assertEquals("Hello, world!", obj["a1"][1]["a2"][0].string)
                assertNull(obj["a1"][1]["a2"][1]["null"].nullObj)

            }

        }

    }

})
