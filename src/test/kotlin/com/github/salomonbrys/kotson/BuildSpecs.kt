package com.github.salomonbrys.kotson

import org.jetbrains.spek.api.shouldBeNull
import org.jetbrains.spek.api.shouldBeTrue
import org.jetbrains.spek.api.shouldEqual
import java.util.*

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

                shouldEqual(42.toJson(), obj["number"])
                shouldEqual('c'.toJson(), obj["char"])
                shouldEqual(true.toJson(), obj["bool"])
                shouldEqual("Hello, world!".toJson(), obj["string"])
                shouldEqual(jsonNull, obj["null"])

            }

            it("should have only the declared values") {
                shouldEqual(5, obj.size())
            }
        }

        on("queried for an inexisting key") {

            it("should throw a NoSuchElementException when queried with getNotNull") {

                shouldThrow<NoSuchElementException> { obj.getNotNull("nothing") }

            }

            it("should return null when queried with get") {

                shouldBeNull(obj["nothing"])

            }

        }

        on("queried for an index") {

            it("should throw an IllegalStateException") {
                shouldThrow<IllegalStateException> { obj[42] }
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

                shouldEqual(42.toJson(), array[0])
                shouldEqual('c'.toJson(), array[1])
                shouldEqual(true.toJson(), array[2])
                shouldEqual("Hello, world!".toJson(), array[3])
                shouldEqual(jsonNull, array[4])

            }

            it("should have only the declared values") {
                shouldEqual(5, array.size())
            }
        }

        on("queried for an inexisting index") {

            it("should throw a IndexOutOfBoundsException") {

                shouldThrow<IndexOutOfBoundsException> { array[5] }

            }

        }

        on("queried for a key") {

            it("should throw an IllegalStateException") {
                shouldThrow<IllegalStateException> { array["nothing"] }
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

                shouldEqual(42, obj["number"].int)
                shouldEqual('c', obj["a1"][0].char)
                shouldBeTrue(obj["a1"][1]["bool"].bool)
                shouldEqual("Hello, world!", obj["a1"][1]["a2"][0].string)
                shouldBeNull(obj["a1"][1]["a2"][1]["null"].nullObj)

            }

        }

    }

})
