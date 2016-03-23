package com.github.salomonbrys.kotson

data class Person(
        val name: String,
        val age: Int
)

data class GenericPerson<out T : Any>(
        val name: String,
        val info: T
)
