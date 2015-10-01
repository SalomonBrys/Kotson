package com.github.salomonbrys.kotson

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.shouldBeFalse
import org.jetbrains.spek.api.shouldBeTrue
import org.jetbrains.spek.api.shouldEqual

class BuilderSpecs : Spek() {

    init {

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
                    shouldBeTrue(obj["object"] === cop["object"])
                    shouldBeTrue(obj["array"] === cop["array"])

                    shouldEqual(42, cop["object"]["answer"].int)
                    shouldEqual("b", cop["array"][1].string)
                }

                it("should not contain post copy direct modification") {
                    shouldEqual(1, cop["number"].int)
                    shouldEqual(2, obj["number"].int)

                    shouldBeFalse("add" in cop)
                    shouldBeTrue("add" in obj)
                }
            }

            on("deep copy") {
                val obj = makeObj()
                val cop = obj.deepCopy()
                change(obj)

                it("should not contain the same objects and arrays") {
                    shouldBeTrue(obj["object"] !== cop["object"])
                    shouldBeTrue(obj["array"] !== cop["array"])

                    shouldEqual(21, cop["object"]["answer"].int)
                    shouldEqual(42, obj["object"]["answer"].int)
                    shouldEqual("c", cop["array"][1].string)
                    shouldEqual("b", obj["array"][1].string)
                }

                it("should not contain post copy direct modification") {
                    shouldEqual(1, cop["number"].int)
                    shouldEqual(2, obj["number"].int)

                    shouldBeFalse("add" in cop)
                    shouldBeTrue("add" in obj)
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
                    shouldBeTrue(arr[1] === cop[1])
                    shouldBeTrue(arr[2] === cop[2])

                    shouldEqual(42, cop[1]["answer"].int)
                    shouldEqual("b", cop[2][1].string)
                }

                it("should not contain post copy direct modification") {
                    shouldEqual(1, cop[0].int)
                    shouldEqual(2, arr[0].int)

                    shouldBeFalse("ok" in cop)
                    shouldBeTrue("ok" in arr)
                }
            }

            on("deep copy") {
                val arr = makeArray()
                val cop = arr.deepCopy()
                change(arr)

                it("should not contain the same objects and arrays") {
                    shouldBeTrue(arr[1] !== cop[1])
                    shouldBeTrue(arr[2] !== cop[2])

                    shouldEqual(21, cop[1]["answer"].int)
                    shouldEqual(42, arr[1]["answer"].int)
                    shouldEqual("c", cop[2][1].string)
                    shouldEqual("b", arr[2][1].string)
                }

                it("should not contain post copy direct modification") {
                    shouldEqual(1, cop[0].int)
                    shouldEqual(2, arr[0].int)

                    shouldBeFalse("ok" in cop)
                    shouldBeTrue("ok" in arr)
                }
            }
        }
    }

}
