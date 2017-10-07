package com.github.salomonbrys.kotson

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CopySpecs : Spek({

    given("a json object") {
        val makeObj = {
            jsonObject(
                    "number" to 1,
                    "object" to jsonObject(
                            "answer" to 21
                    ),
                    "array" to jsonArray("a", "c")
            )
        }

        val change = { obj: JsonObject ->
            obj["number"] = 2
            obj["object"]["answer"] = 42
            obj["array"][1] = "b"
            obj["add"] = "ok"
        }

        on("shallow copy") {
            val obj = makeObj()
            val cop = obj.shallowCopy()
            change(obj)

            it("should contain the same objects and arrays") {
                assertTrue(obj["object"] === cop["object"])
                assertTrue(obj["array"] === cop["array"])

                assertEquals(42, cop["object"]["answer"].int)
                assertEquals("b", cop["array"][1].string)
            }

            it("should not contain post copy direct modification") {
                assertEquals(1, cop["number"].int)
                assertEquals(2, obj["number"].int)

                assertFalse("add" in cop)
                assertTrue("add" in obj)
            }
        }

        on("deep copy") {
            val obj = makeObj()
            val cop = obj.deepCopy()
            change(obj)

            it("should not contain the same objects and arrays") {
                assertTrue(obj["object"] !== cop["object"])
                assertTrue(obj["array"] !== cop["array"])

                assertEquals(21, cop["object"]["answer"].int)
                assertEquals(42, obj["object"]["answer"].int)
                assertEquals("c", cop["array"][1].string)
                assertEquals("b", obj["array"][1].string)
            }

            it("should not contain post copy direct modification") {
                assertEquals(1, cop["number"].int)
                assertEquals(2, obj["number"].int)

                assertFalse("add" in cop)
                assertTrue("add" in obj)
            }
        }
    }


    given("a json array") {
        val makeArray = {
            jsonArray(
                    1,
                    jsonObject(
                            "answer" to 21
                    ),
                    jsonArray("a", "c")
            )
        }

        val change = { array: JsonArray ->
            array[0] = 2
            array[1]["answer"] = 42
            array[2][1] = "b"
            array += "ok"
        }

        on("shallow copy") {
            val arr = makeArray()
            val cop = arr.shallowCopy()
            change(arr)

            it("should contain the same objects and arrays") {
                assertTrue(arr[1] === cop[1])
                assertTrue(arr[2] === cop[2])

                assertEquals(42, cop[1]["answer"].int)
                assertEquals("b", cop[2][1].string)
            }

            it("should not contain post copy direct modification") {
                assertEquals(1, cop[0].int)
                assertEquals(2, arr[0].int)

                assertFalse("ok" in cop)
                assertTrue("ok" in arr)
            }
        }

        on("deep copy") {
            val arr = makeArray()
            val cop = arr.deepCopy()
            change(arr)

            it("should not contain the same objects and arrays") {
                assertTrue(arr[1] !== cop[1])
                assertTrue(arr[2] !== cop[2])

                assertEquals(21, cop[1]["answer"].int)
                assertEquals(42, arr[1]["answer"].int)
                assertEquals("c", cop[2][1].string)
                assertEquals("b", arr[2][1].string)
            }

            it("should not contain post copy direct modification") {
                assertEquals(1, cop[0].int)
                assertEquals(2, arr[0].int)

                assertFalse("ok" in cop)
                assertTrue("ok" in arr)
            }
        }
    }

})
