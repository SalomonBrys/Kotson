package com.github.salomonbrys.kotson

import org.jetbrains.spek.api.Spek
import java.lang.reflect.ParameterizedType
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TypeTokenSpecs : Spek ({

    given("a non generic type") {
        on("typeToken") {
            it("should give the type's class") {
                assertEquals(String::class.java, typeToken<String>())
            }
        }
    }

    given("a one parameter Any generic type") {

        on("wildcard typeToken") {
            it("should give the type's class") {
                assertEquals(List::class.java, typeToken<List<*>>())
            }
        }

        on("specialized typeToken") {
            it("should give a ParameterizedType") {
                assertTrue(typeToken<List<String>>() is ParameterizedType)
            }
        }

    }

    given("a two parameter Any generic type") {

        on("full wildcard typeToken") {
            it("should give the type's class") {
                assertEquals(Map::class.java, typeToken<Map<*, *>>())
            }
        }

        on("full specialized typeToken") {
            it("should give a ParameterizedType") {
                assertTrue(typeToken<Map<String, String>>() is ParameterizedType)
            }
        }

        on("semi specialized typeToken") {
            it("should throw an exception T-*") {
                assertFailsWith<IllegalArgumentException> {
                    typeToken<Map<String, *>>()
                }
            }
            it("should throw an exception *-T") {
                assertFailsWith<IllegalArgumentException> {
                    typeToken<Map<*, String>>()
                }
            }
        }

    }

    given("a one (interface) parameter generic type") {

        on("wildcard typeToken") {
            it("should give the type's class") {
                assertEquals(SingleBase::class.java, typeToken<SingleBase<*>>())
            }
        }

        on("specialized typeToken") {
            it("should give a ParameterizedType") {
                assertTrue(typeToken<SingleBase<Value>>() is ParameterizedType)
            }
        }

    }

    given("a one (class) parameter generic type") {

        on("wildcard typeToken") {
            it("should give the type's class") {
                assertEquals(SingleValue::class.java, typeToken<SingleValue<*>>())
            }
        }

        on("base specialized typeToken") {
            it("should give the type's class too") {
                assertEquals(SingleValue::class.java, typeToken<SingleValue<Value>>())
            }
        }

        on("specialized typeToken") {
            it("should give a ParameterizedType") {
                assertTrue(typeToken<SingleValue<SubValue>>() is ParameterizedType)
            }
        }

    }

    given("a two (Any, interface) parameter generic type") {

        on("full wildcard typeToken") {
            it("should give the type's class") {
                assertEquals(AnyBaseTuple::class.java, typeToken<AnyBaseTuple<*, *>>())
            }
        }

        on("full specialized typeToken") {
            it("should give a ParameterizedType") {
                assertTrue(typeToken<AnyBaseTuple<String, Value>>() is ParameterizedType)
            }
        }

        on("semi specialized typeToken") {
            it("should throw an exception T-*") {
                assertFailsWith<IllegalArgumentException> {
                    typeToken<AnyBaseTuple<String, *>>()
                }
            }
            it("should throw an exception *-T") {
                assertFailsWith<IllegalArgumentException> {
                    typeToken<AnyBaseTuple<*, Value>>()
                }
            }
        }

    }

    given("a two (interface, interface) parameter generic type") {

        on("full wildcard typeToken") {
            it("should give the type's class") {
                assertEquals(BaseBaseTuple::class.java, typeToken<BaseBaseTuple<*, *>>())
            }
        }

        on("full specialized typeToken") {
            it("should give a ParameterizedType") {
                assertTrue(typeToken<BaseBaseTuple<Value, Value>>() is ParameterizedType)
            }
        }

        on("semi specialized typeToken") {
            it("should throw an exception") {
                assertFailsWith<IllegalArgumentException> {
                    typeToken<BaseBaseTuple<Value, *>>()
                }
                assertFailsWith<IllegalArgumentException> {
                    typeToken<BaseBaseTuple<*, Value>>()
                }
            }
        }

    }

    given("a two (interface, class) parameter generic type") {

        on("full wildcard typeToken") {
            it("should give the type's class") {
                assertEquals(BaseValueTuple::class.java, typeToken<BaseValueTuple<*, *>>())
            }
        }

        on("full specialized typeToken") {
            it("should give a ParameterizedType") {
                assertTrue(typeToken<BaseValueTuple<SubValue, SubValue>>() is ParameterizedType)
            }
        }

        on("semi specialized typeToken") {
            it("should give a ParameterizedType T-*") {
                assertTrue(typeToken<BaseValueTuple<SubValue, *>>() is ParameterizedType)
            }
            it("should throw an exception *-T") {
                assertFailsWith<IllegalArgumentException> {
                    typeToken<BaseValueTuple<*, SubValue>>()
                }
            }
        }

    }

    given("a two (class, class) parameter generic type") {

        on("full wildcard typeToken") {
            it("should give the type's class") {
                assertEquals(ValueValueTuple::class.java, typeToken<ValueValueTuple<*, *>>())
            }
        }

        on("full specialized typeToken") {
            it("should give a ParameterizedType") {
                assertTrue(typeToken<ValueValueTuple<SubValue, SubValue>>() is ParameterizedType)
            }
        }

        on("semi specialized typeToken") {
            it("should give a ParameterizedType T-*") {
                assertTrue(typeToken<ValueValueTuple<SubValue, *>>() is ParameterizedType)
            }
            it("should give a ParameterizedType *-T") {
                assertTrue(typeToken<ValueValueTuple<*, SubValue>>() is ParameterizedType)
            }
        }

    }

}) {

    interface Base

    open class Value : Base

    class SubValue : Value()


    class SingleBase<out T : Base>

    class SingleValue<out T : Value>

    class AnyBaseTuple<T : Any, out U : Base>

    class BaseBaseTuple<T : Base, out U : Base>

    class BaseValueTuple<T : Base, out U : Value>

    class ValueValueTuple<T : Value, out U : Value>

}
